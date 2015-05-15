package edu.washington.dkhaw.quizdroid;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private List<String> answers;
    private int correct;

    public Question(String question, List<String> answers, int correct) {
        this.question = question;
        this.answers = answers;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public int getCorrect() {
        return correct;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }
}
