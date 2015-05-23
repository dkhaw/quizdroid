package edu.washington.dkhaw.quizdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    public static final String TOPIC = "edu.washington.dkhaw.quizdroid.TOPIC";
    public static String url = "";
    public static int interval = -1;
    private static final int PREFERENCES_RESULT = 1;
    private PendingIntent pendingIntent;
    private QuizApp.TopicRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);

        File myFile = new File(getFilesDir().getAbsolutePath(), "/questions.json");

        // check if data.json file exists in files directory
        if (myFile.exists()) {
            try {
                FileInputStream fis = openFileInput("questions.json");
                repo = new JSONRepository(fis);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        // Fetch data.json in assets/ folder
        } else {
            AssetManager assetManager = getAssets();
            try {
                InputStream input = assetManager.open("questions.json");
                repo = new JSONRepository(input);
                // use hardcoded data as a backup
            } catch (IOException | JSONException e) {
                repo = new HardcodedRepository();
            }
        }

        //DownloadService.changeAlarm(this, true);

        String[] topics = repo.getAllTopics();
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                topics);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = repo.getTopic(parent.getItemAtPosition(position).toString());
                Intent next = new Intent(getApplicationContext(), QuizActivity.class);
                next.putExtra(TOPIC, topic);
                startActivity(next);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                openPreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void openPreferences() {
        Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
        startActivityForResult(i, PREFERENCES_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (airplaneMode(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You are currently in Airplane Mode. You must turn this off before a download can occur.")
                    .setTitle("Airplane Mode");
            builder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (!online()) {
            Toast.makeText(this, "Error downloading: You are currently offline", Toast.LENGTH_LONG).show();
        } else {
            if (requestCode == PREFERENCES_RESULT) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                url = sharedPrefs.getString("prefDataUrl", "NODATAURL");
                interval = Integer.valueOf(sharedPrefs.getString("prefDownloadInterval", "NODOWNLOADINTERVAL")) * 60000;
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            }
        }
    }

    private static boolean airplaneMode(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    public boolean online() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DownloadManager dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                // if the downloadID exists
                if (downloadID != 0) {
                    // Check status
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = dm.query(query);
                    if(c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch(status) {
                            case DownloadManager.STATUS_PAUSED:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                ParcelFileDescriptor file;
                                StringBuffer sb = new StringBuffer("");
                                int ch;
                                try {
                                    file = dm.openDownloadedFile(downloadID);
                                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());
                                    while((ch = fis.read()) != -1) {
                                        sb.append((char)ch);
                                    }
                                    String data = sb.toString();
                                    try {
                                        File newFile = new File(getFilesDir().getAbsolutePath(), "questions.json");
                                        FileOutputStream fos = new FileOutputStream(newFile);
                                        fos.write(data.getBytes());
                                        fos.close();
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("MainActivity","json downloaded:" + data);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                                builder.setMessage("Download failed. Would you like to double check your URL and try again?")
                                        .setTitle("Retry");
                                builder.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
                                        startActivityForResult(i, PREFERENCES_RESULT);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();                                break;
                        }
                    }
                }
            }
        }
    };
}
