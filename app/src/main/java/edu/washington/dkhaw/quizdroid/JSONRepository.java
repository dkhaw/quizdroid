package edu.washington.dkhaw.quizdroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;


public class JSONRepository implements TopicRepository {
    private HashMap<String, Topic> topics;

    public String[] getAllTopics() {
        String[] allTopics = topics.keySet().toArray(new String[10]);
        return allTopics;
    }

    public Topic getTopic(String topic) {
        return topics.get(topic);
    }

    public JSONRepository(InputStream input) throws IOException, JSONException {
        topics = new HashMap<>();
        String json = readJSONFile(input);
        JSONArray jsonTopics = new JSONArray(json);
        // separate into topics
        for (int i = 0; i < jsonTopics.length(); i++) {
            JSONObject jsonObject = jsonTopics.getJSONObject(i);
            String title = jsonObject.getString("title");
            String desc = jsonObject.getString("desc");
            JSONArray jsonQuestions = jsonObject.getJSONArray("questions");
            List<Question> questions = new ArrayList<>();
            // separate into questions
            for (int j = 0; j < jsonQuestions.length(); j++) {
                JSONObject jsonObject1 = jsonQuestions.getJSONObject(j);
                String questionText = jsonObject1.getString("text");
                int correct = Integer.parseInt(jsonObject1.getString("answer")) - 1;
                JSONArray jsonAnswers = jsonObject1.getJSONArray("answers");
                List<String> answers = new ArrayList<>();
                // separate into answers
                for (int k = 0; k < jsonAnswers.length(); k++) {
                    answers.add(jsonAnswers.getString(k));
                }
                Question question = new Question(questionText, answers, correct);
                questions.add(question);
            }
            Topic topic = new Topic(title, desc, questions);
            topics.put(title, topic);
        }
    }

    // returns JSON String
    public String readJSONFile(InputStream input) throws IOException {
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();
        return new String(buffer, "UTF-8");
    }
}
