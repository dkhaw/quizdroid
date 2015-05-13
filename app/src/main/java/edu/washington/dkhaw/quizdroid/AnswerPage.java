package edu.washington.dkhaw.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnswerPage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_page);

        Intent intent = getIntent();
        final String topic = intent.getStringExtra(MainActivity.TOPIC);
        final int count = intent.getIntExtra(QuestionPage.COUNT, -1);
        final int totalQuestions = intent.getIntExtra(QuestionPage.TOTAL_QUESTIONS, -1);
        final int totalCorrect = intent.getIntExtra(QuestionPage.TOTAL_CORRECT, -1);
        final String correctAnswer = intent.getStringExtra(QuestionPage.CORRECT_ANSWER);
        final String selectedAnswer = intent.getStringExtra(QuestionPage.SELECTED_ANSWER);

        TextView tvSelectedAnswer = (TextView) findViewById(R.id.tvSelectedAnswer);
        TextView tvCorrectAnswer = (TextView) findViewById(R.id.tvCorrectAnswer);
        TextView tvStats = (TextView) findViewById(R.id.tvStats);
        Button btnNext = (Button) findViewById(R.id.btnNext);

        tvSelectedAnswer.setText("Selected answer: " + selectedAnswer);
        tvCorrectAnswer.setText("Correct answer: " + correctAnswer);
        tvStats.setText("You have " + totalCorrect + " out of " + totalQuestions + " correct");

        if (totalQuestions - 1 == count) {
            btnNext.setText("Finish");
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuestions - 1 == count) { // quiz finished
                    Intent next = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(next);
                } else {
                    Intent next = new Intent(getApplicationContext(), QuestionPage.class);
                    next.putExtra(MainActivity.TOPIC, topic);
                    next.putExtra(QuestionPage.COUNT, count + 1);
                    next.putExtra(QuestionPage.TOTAL_CORRECT, totalCorrect);
                    next.putExtra(QuestionPage.SELECTED_ANSWER, selectedAnswer);
                    next.putExtra(QuestionPage.CORRECT_ANSWER, correctAnswer);
                    startActivity(next);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer_page, menu);
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
