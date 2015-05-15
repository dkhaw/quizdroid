package edu.washington.dkhaw.quizdroid;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class QuizActivity extends ActionBarActivity implements TopicOverviewFragment.OnBeginListener,
        QuestionFragment.OnSubmitListener, AnswerFragment.OnNextListener {

    public static final String COUNT = "edu.washington.dkhaw.quizdroid.COUNT";
    public static final String TOTAL_QUESTIONS = "edu.washington.dkhaw.quizdroid.TOTAL_QUESTIONS";
    public static final String TOTAL_CORRECT = "edu.washington.dkhaw.quizdroid.TOTAL_CORRECT";
    public static final String SELECTED_ANSWER = "edu.washington.dkhaw.quizdroid.SELECTED_ANSWER";
    public static final String CORRECT_ANSWER = "edu.washington.dkhaw.quizdroid.CORRECT_ANSWER";

    private Topic topic;
    private int count;
    private int totalQuestions;
    private int totalCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        topic = (Topic) intent.getSerializableExtra(MainActivity.TOPIC);
        count = 0;
        totalCorrect = 0;
        totalQuestions = topic.getQuestions().size();

        Bundle args = new Bundle();
        args.putSerializable(MainActivity.TOPIC, topic);
        Fragment topicOverviewFragment = new TopicOverviewFragment();
        topicOverviewFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, topicOverviewFragment)
                .commit();
    }

    @Override
    public void onBegin() {
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.TOPIC, topic);
        args.putInt(QuizActivity.COUNT, count);
        Fragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, questionFragment)
                .commit();
    }

    @Override
    public void onSubmit(String selectedAnswer) {
        int correct = topic.getQuestions().get(count).getCorrect();
        String correctAnswer = topic.getQuestions().get(count).getAnswers().get(correct);
        if (selectedAnswer.equals(correctAnswer)) {
            totalCorrect++;
        }

        Bundle args = new Bundle();
        args.putInt(QuizActivity.COUNT, count);
        args.putInt(QuizActivity.TOTAL_QUESTIONS, totalQuestions);
        args.putInt(QuizActivity.TOTAL_CORRECT, totalCorrect);
        args.putString(QuizActivity.SELECTED_ANSWER, selectedAnswer);
        args.putString(QuizActivity.CORRECT_ANSWER, correctAnswer);
        Fragment answerFragment = new AnswerFragment();
        answerFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, answerFragment)
                .commit();
    }

    @Override
    public void onNext() {
        if (count == totalQuestions - 1) {
            finish();
        } else {
            count++;
            onBegin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
