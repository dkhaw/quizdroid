package edu.washington.dkhaw.quizdroid;

import java.util.List;

public class Topic {
    private String title;
    private String desc;
    private List<Question> questions;

    public Topic() {}

    public Topic(String title, String desc, List<Question> questions) {
        this.title = title;
        this.desc = desc;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
