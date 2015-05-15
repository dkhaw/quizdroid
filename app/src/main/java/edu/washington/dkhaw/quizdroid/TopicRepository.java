package edu.washington.dkhaw.quizdroid;

import java.util.List;

public interface TopicRepository {
    public String[] getAllTopics();
    public Topic getTopicByKeyword(String keyword);
}