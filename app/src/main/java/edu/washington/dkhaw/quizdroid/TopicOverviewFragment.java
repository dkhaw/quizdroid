package edu.washington.dkhaw.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TopicOverviewFragment extends Fragment {

    private Topic topic;
    private OnBeginListener beginListener;

    public TopicOverviewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            topic = (Topic) args.getSerializable(MainActivity.TOPIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_overview, container, false);

        TextView tvTopic = (TextView) view.findViewById(R.id.tvTopic);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        TextView tvNumQuestions = (TextView) view.findViewById(R.id.tvNumQuestions);
        Button btnBegin = (Button) view.findViewById(R.id.btnBegin);

        tvTopic.setText(topic.getTitle());
        tvDesc.setText(topic.getDesc());
        tvNumQuestions.setText(topic.getQuestions().size() + " Questions");
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginListener.onBegin();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            beginListener = (OnBeginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBeginListener");
        }
    }

    public interface OnBeginListener {
        public void onBegin();
    }
}