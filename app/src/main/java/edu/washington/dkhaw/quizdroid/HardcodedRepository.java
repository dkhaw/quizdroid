package edu.washington.dkhaw.quizdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HardcodedRepository implements TopicRepository {
    private HashMap<String, Topic> topics;

    public String[] getAllTopics() {
        String[] allTopics = topics.keySet().toArray(new String[10]);
        return allTopics;
    }

    public Topic getTopicByKeyword(String keyword) {
        return topics.get(keyword);
    }

    public HardcodedRepository() {
        topics = new HashMap<>();

        final List<Question> mQuestions = new ArrayList<>();
        List<String> mAnswers1 = new ArrayList<>();
        List<String> mAnswers2 = new ArrayList<>();
        List<String> mAnswers3 = new ArrayList<>();
        mAnswers1.add("0");
        mAnswers1.add("2");
        mAnswers1.add("4");
        mAnswers1.add("22");
        mQuestions.add(new Question("What is 2 + 2?", mAnswers1, 2));
        mAnswers2.add("0");
        mAnswers2.add("2");
        mAnswers2.add("9");
        mAnswers2.add("11");
        mQuestions.add(new Question("What is 2 + (3 * 3) * 0?", mAnswers2, 1));
        mAnswers3.add("3");
        mAnswers3.add("6");
        mAnswers3.add("9");
        mAnswers3.add("12");
        mQuestions.add(new Question("What is 6 / 2 * (1 + 2)?", mAnswers3, 2));
        Topic mTopic = new Topic("Math", "A game of numbers", mQuestions);
        topics.put("Math", mTopic);

        final List<Question> pQuestions = new ArrayList<>();
        List<String> pAnswers1 = new ArrayList<>();
        List<String> pAnswers2 = new ArrayList<>();
        List<String> pAnswers3 = new ArrayList<>();
        pAnswers1.add("5");
        pAnswers1.add("7");
        pAnswers1.add("9");
        pAnswers1.add("10");
        pQuestions.add(new Question("How many colors are there in the spectrum when white light is separated?", pAnswers1, 1));
        pAnswers2.add("Very high pressure");
        pAnswers2.add("Very low pressure");
        pAnswers2.add("Very high temperature");
        pAnswers2.add("Very low temperature");
        pQuestions.add(new Question("What is studied in the science of cryogenics?", pAnswers2, 3));
        pAnswers3.add("4 minutes");
        pAnswers3.add("8 minutes");
        pAnswers3.add("12 minutes");
        pAnswers3.add("16 minutes");
        pQuestions.add(new Question("About how long does it take light from the Sun to reach us?", pAnswers3, 1));
        Topic pTopic = new Topic("Physics", "May the force be with you", pQuestions);
        topics.put("Physics", pTopic);

        final List<Question> vQuestions = new ArrayList<>();
        List<String> vAnswers1 = new ArrayList<>();
        List<String> vAnswers2 = new ArrayList<>();
        List<String> vAnswers3 = new ArrayList<>();
        vAnswers1.add("Benjamin");
        vAnswers1.add("William");
        vAnswers1.add("Henry");
        vAnswers1.add("Andrew");
        vQuestions.add(new Question("What is Peter Parker's middle name?", vAnswers1, 0));
        vAnswers2.add("Stark Tower");
        vAnswers2.add("Fantastic Headquarters");
        vAnswers2.add("Baxter Building");
        vAnswers2.add("Xavier Institute");
        vQuestions.add(new Question("The Fantastic Four have their headquarters in what building?", vAnswers2, 2));
        vAnswers3.add("World War I");
        vAnswers3.add("World War II");
        vAnswers3.add("Cold War");
        vAnswers3.add("American Civil War");
        vQuestions.add(new Question("Captain America was frozen in which war?", vAnswers3, 1));
        Topic vTopic = new Topic("Marvel", "My spidey sense is tingling", vQuestions);
        topics.put("Marvel", vTopic);
    }
}
