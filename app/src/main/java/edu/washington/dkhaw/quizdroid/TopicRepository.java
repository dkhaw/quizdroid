package edu.washington.dkhaw.quizdroid;

public interface TopicRepository {
    public String[] getAllTopics();
    public Topic getTopic(String topic);
}