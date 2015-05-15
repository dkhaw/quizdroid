package edu.washington.dkhaw.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class QuestionFragment extends Fragment {

    private OnSubmitListener submitListener;
    private Topic topic;
    private int count;

    public QuestionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            topic = (Topic) args.getSerializable(MainActivity.TOPIC);
            count = args.getInt(QuizActivity.COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_question, container, false);

        TextView tvQuestion = (TextView) view.findViewById(R.id.tvQuestion);
        tvQuestion.setText(topic.getQuestions().get(count).getQuestion());

        final RadioGroup rgAnswers = (RadioGroup) view.findViewById(R.id.rgAnswers);
        List<String> answers = topic.getQuestions().get(count).getAnswers();
        for (int i = 0; i < answers.size(); ++i) {
            RadioButton answer = new RadioButton(this.getActivity());
            answer.setText(answers.get(i));
            rgAnswers.addView(answer);
        }

        final Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) view.findViewById(rgAnswers.getCheckedRadioButtonId());
                String selectedAnswer = rb.getText().toString();
                submitListener.onSubmit(selectedAnswer);
            }
        });

        // enables submit button after a radio button has been selected
        rgAnswers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnSubmit.setEnabled(true);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            submitListener = (OnSubmitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSubmitListener");
        }
    }

    public interface OnSubmitListener {
        public void onSubmit(String selectedAnswer);
    }
}
