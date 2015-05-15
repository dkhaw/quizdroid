package edu.washington.dkhaw.quizdroid;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends ActionBarActivity implements TopicOverviewFragment.OnBeginListener,
        QuestionFragment.OnSubmitListener, AnswerFragment.OnNextListener {

    public static final String TOPIC = "edu.washington.dkhaw.quizdroid.TOPIC";
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
        String title = intent.getStringExtra(MainActivity.TITLE);

        String desc = "";
        if (title.equals("Math")) {
            desc = "A game of numbers";
        } else if (title.equals("Physics")) {
            desc = "May the Force be with you";
        } else if (title.equals("Marvel")) {
            desc = "My spidey sense is tingling";
        }

        final List<Question> questions = new ArrayList<>();
        List<String> answers1 = new ArrayList<>();
        List<String> answers2 = new ArrayList<>();
        List<String> answers3 = new ArrayList<>();

        if (title.equals("Math")) {
            answers1.add("0");
            answers1.add("2");
            answers1.add("4");
            answers1.add("22");
            questions.add(new Question("What is 2 + 2?", answers1, 2));

            answers2.add("0");
            answers2.add("2");
            answers2.add("9");
            answers2.add("11");
            questions.add(new Question("What is 2 + (3 * 3) * 0?", answers2, 1));

            answers3.add("3");
            answers3.add("6");
            answers3.add("9");
            answers3.add("12");
            questions.add(new Question("What is 6 / 2 * (1 + 2)?", answers3, 2));

        } else if (title.equals("Physics")) {
            answers1.add("5");
            answers1.add("7");
            answers1.add("9");
            answers1.add("10");
            questions.add(new Question("How many colors are there in the spectrum when white light is separated?", answers1, 1));

            answers2.add("Very high pressure");
            answers2.add("Very low pressure");
            answers2.add("Very high temperature");
            answers2.add("Very low temperature");
            questions.add(new Question("What is studied in the science of cryogenics?", answers2, 3));

            answers3.add("4 minutes");
            answers3.add("8 minutes");
            answers3.add("12 minutes");
            answers3.add("16 minutes");
            questions.add(new Question("About how long does it take light from the Sun to reach us?", answers3, 1));

        } else if (title.equals("Marvel")) {
            answers1.add("Benjamin");
            answers1.add("William");
            answers1.add("Henry");
            answers1.add("Andrew");
            questions.add(new Question("What is Peter Parker's middle name?", answers1, 0));

            answers2.add("Stark Tower");
            answers2.add("Fantastic Headquarters");
            answers2.add("Baxter Building");
            answers2.add("Xavier Institute");
            questions.add(new Question("The Fantastic Four have their headquarters in what building?", answers2, 2));

            answers3.add("World War I");
            answers3.add("World War II");
            answers3.add("Cold War");
            answers3.add("American Civil War");
            questions.add(new Question("Captain America was frozen in which war?", answers3, 1));
        }

        topic = new Topic(title, desc, questions);
        count = 0;
        totalCorrect = 0;
        totalQuestions = topic.getQuestions().size();

        Bundle args = new Bundle();
        args.putSerializable(QuizActivity.TOPIC, topic);

        Fragment topicOverviewFragment = new TopicOverviewFragment();
        topicOverviewFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, topicOverviewFragment)
                .commit();
    }

    @Override
    public void onBegin() {
        Bundle args = new Bundle();
        args.putSerializable(QuizActivity.TOPIC, topic);
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
