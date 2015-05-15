package edu.washington.dkhaw.quizdroid;

import android.app.Application;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;


public class QuizApp extends Application {
    private static QuizApp instance;
    private TopicRepository repo;

    /* Protect QuizApp at runtime */
    public QuizApp() throws JSONException {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Cannot create more than one QuizApp");
        }
        // Fetch data.json in assets/ folder
        try {
            InputStream input = getAssets().open("questions.json");
            repo = new JSONRepository(input);
            throw new JSONException("JSONException");
            // use hardcoded data as a backup
        } catch (FileNotFoundException e) {
            repo = new HardcodedRepository();
        } catch (IOException e) {
            repo = new HardcodedRepository();
        } catch (JSONException e) {
            repo = new HardcodedRepository();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("QuizApp", "QuizApp fired");
    }

    public TopicRepository getTopicRepository() {
        return repo;
    }
}
