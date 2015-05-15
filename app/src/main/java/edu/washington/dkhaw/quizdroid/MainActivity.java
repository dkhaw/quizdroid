package edu.washington.dkhaw.quizdroid;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    public static final String TOPIC = "edu.washington.dkhaw.quizdroid.TOPIC";
    private QuizApp.TopicRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Fetch data.json in assets/ folder
        //QuizApp app = (QuizApp) getApplication();
        AssetManager assetManager = getAssets();
        try {
            InputStream input = assetManager.open("questions.json");
            repo = new JSONRepository(input);
            // use hardcoded data as a backup
        } catch (IOException | JSONException e) {
            repo = new HardcodedRepository();
        }

        //final TopicRepository repo = app.getTopicRepository();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
