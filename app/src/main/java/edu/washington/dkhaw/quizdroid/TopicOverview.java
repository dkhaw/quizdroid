package edu.washington.dkhaw.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class TopicOverview extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_overview);
        Intent intent = getIntent();
        final String topic = intent.getStringExtra(MainActivity.TOPIC);

        TextView tvTopic = (TextView) findViewById(R.id.tvTopic);
        tvTopic.setText(topic);

        TextView tvDesc = (TextView) findViewById(R.id.tvDesc);
        TextView tvNumQuestions = (TextView) findViewById(R.id.tvNumQuestions);
        if (topic.equals("Math")) {
            tvDesc.setText("A game of numbers");
            tvNumQuestions.setText("3 Questions");
        } else if (topic.equals("Physics")) {
            tvDesc.setText("May the Force be with you");
            tvNumQuestions.setText("3 Questions");
        } else if (topic.equals("Marvel")) {
            tvDesc.setText("My spidey sense is tingling");
            tvNumQuestions.setText("3 Questions");
        }

        Button begin = (Button) findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(getApplicationContext(), QuestionPage.class);
                next.putExtra(MainActivity.TOPIC, topic);
                startActivity(next);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_overview, menu);
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
