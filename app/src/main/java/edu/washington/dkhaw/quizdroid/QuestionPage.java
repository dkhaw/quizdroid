package edu.washington.dkhaw.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class QuestionPage extends ActionBarActivity {

    public static final String COUNT = "edu.washington.dkhaw.quizdroid.COUNT";
    public static final String TOTAL_CORRECT = "edu.washington.dkhaw.quizdroid.TOTAL_CORRECT";
    public static final String TOTAL_QUESTIONS = "edu.washington.dkhaw.quizdroid.TOTAL_QUESTIONS";
    public static final String CORRECT_ANSWER = "edu.washington.dkhaw.quizdroid.CORRECT_ANSWER";
    public static final String SELECTED_ANSWER = "edu.washington.dkhaw.quizdroid.SELECTED_ANSWER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        Intent intent = getIntent();
        final String topic = intent.getStringExtra(MainActivity.TOPIC);
        final int count = intent.getIntExtra(COUNT, 0);
        final int totalCorrect = intent.getIntExtra(TOTAL_CORRECT, 0);

        final List<Question> questions = new ArrayList<>();
        List<String> answers1 = new ArrayList<>();
        List<String> answers2 = new ArrayList<>();
        List<String> answers3 = new ArrayList<>();

        if (topic.equals("Math")) {
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

        } else if (topic.equals("Physics")) {
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

        } else if (topic.equals("Marvel")) {
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

        TextView tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        final RadioGroup rg = (RadioGroup) findViewById(R.id.rgAnswers);
        final RadioButton rbAnswer1 = (RadioButton) findViewById(R.id.rbAnswer1);
        final RadioButton rbAnswer2 = (RadioButton) findViewById(R.id.rbAnswer2);
        final RadioButton rbAnswer3 = (RadioButton) findViewById(R.id.rbAnswer3);
        final RadioButton rbAnswer4 = (RadioButton) findViewById(R.id.rbAnswer4);
        final Button btnSubmit = (Button) findViewById(R.id.btnSubmit);

        tvQuestion.setText(questions.get(count).getQuestion());
        rbAnswer1.setText(questions.get(count).getAnswers().get(0));
        rbAnswer2.setText(questions.get(count).getAnswers().get(1));
        rbAnswer3.setText(questions.get(count).getAnswers().get(2));
        rbAnswer4.setText(questions.get(count).getAnswers().get(3));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                String selectedAnswer = (String) rb.getText();
                int correct = questions.get(count).getCorrect();
                String correctAnswer = questions.get(count).getAnswers().get(correct);
                int finalTotalCorrect = totalCorrect;
                if (selectedAnswer.equals(correctAnswer)) {
                    finalTotalCorrect++;
                }

                Intent next = new Intent(getApplicationContext(), AnswerPage.class);
                next.putExtra(MainActivity.TOPIC, topic);
                next.putExtra(COUNT, count);
                next.putExtra(TOTAL_QUESTIONS, questions.size());
                next.putExtra(TOTAL_CORRECT, finalTotalCorrect);
                next.putExtra(SELECTED_ANSWER, selectedAnswer);
                next.putExtra(CORRECT_ANSWER, correctAnswer);
                startActivity(next);
            }
        });

        // enables submit button after a radio button has been selected
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnSubmit.setEnabled(true);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_page, menu);
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
