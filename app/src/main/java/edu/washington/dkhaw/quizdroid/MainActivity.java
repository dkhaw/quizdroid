package edu.washington.dkhaw.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
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
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    public static final String TOPIC = "edu.washington.dkhaw.quizdroid.TOPIC";
    public static String url = "";
    public static int interval = -1;
    private static final int PREFERENCES_RESULT = 1;
    private PendingIntent pendingIntent;
    private DownloadManager dm;
    private QuizApp.TopicRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        //IntentFilter filter = new IntentFilter();
        //filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); // Add more filters here that you want the receiver to listen to
        //registerReceiver(receiver, filter);

        File myFile = new File(getFilesDir().getAbsolutePath(), "/data.json");

        // check if data.json file exists in files directory
        if (myFile.exists()) {
            Log.i("MyApp", "data.json DOES exist");

            try {
                FileInputStream fis = openFileInput("data.json");
                repo = new JSONRepository(fis);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        } else {
            // Fetch data.json in assets/ folder
            AssetManager assetManager = getAssets();
            try {
                InputStream input = assetManager.open("questions.json");
                repo = new JSONRepository(input);
                // use hardcoded data as a backup
            } catch (IOException | JSONException e) {
                repo = new HardcodedRepository();
            }
        }

        // DownloadService.changeAlarm(this, true);

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

        if (requestCode == PREFERENCES_RESULT) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            url = sharedPrefs.getString("prefDataUrl", "NODATAURL");
            interval = Integer.valueOf(sharedPrefs.getString("prefDownloadInterval", "NODOWNLOADINTERVAL")) * 60000;
            Toast.makeText(this, url, Toast.LENGTH_SHORT).show(); // to be removed later
            /*AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);*/
        }
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
            // start a DownloadManager
        }
    };
}
