package edu.washington.dkhaw.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AnswerFragment extends Fragment {

    private OnNextListener nextListener;
    private int count;
    private int totalQuestions;
    private int totalCorrect;
    private String correctAnswer;
    private String selectedAnswer;

    public AnswerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            count = args.getInt(QuizActivity.COUNT);
            totalQuestions = args.getInt(QuizActivity.TOTAL_QUESTIONS);
            totalCorrect = args.getInt(QuizActivity.TOTAL_CORRECT);
            selectedAnswer = args.getString(QuizActivity.SELECTED_ANSWER);
            correctAnswer = args.getString(QuizActivity.CORRECT_ANSWER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_answer, container, false);

        TextView tvSelectedAnswer = (TextView) view.findViewById(R.id.tvSelectedAnswer);
        tvSelectedAnswer.setText("Selected answer: " + selectedAnswer);
        TextView tvCorrectAnswer = (TextView) view.findViewById(R.id.tvCorrectAnswer);
        tvCorrectAnswer.setText("Correct answer: " + correctAnswer);
        TextView tvStats = (TextView) view.findViewById(R.id.tvStats);
        tvStats.setText("You have " + totalCorrect + " out of " + totalQuestions + " correct");
        final Button btnNext = (Button) view.findViewById(R.id.btnNext);
        if (count == totalQuestions - 1) {
            btnNext.setText("Finish");
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextListener.onNext();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            nextListener = (OnNextListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNextListener");
        }
    }

    public interface OnNextListener {
        public void onNext();
    }

}
