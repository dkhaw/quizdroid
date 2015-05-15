package edu.washington.dkhaw.quizdroid;

import android.app.Application;
import android.util.Log;



public class QuizApp extends Application {
    private static QuizApp instance = null;

    /* Protect QuizApp at runtime */
    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Cannot create more than one QuizApp");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("QuizApp", "QuizApp fired");
    }

    public interface TopicRepository {
        public String[] getAllTopics();
        public Topic getTopic(String title);
    }
}
