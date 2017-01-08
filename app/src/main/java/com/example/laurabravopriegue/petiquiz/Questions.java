package com.example.laurabravopriegue.petiquiz;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class Questions {
    public static ArrayList<Question> mQuestionBank;
    public static Boolean questionsLoaded = false;

    public static ArrayList<Question> getQuestions() {
        if (questionsLoaded) {
            return mQuestionBank;
        }
        else {
            return null;
        }
    }

    public static Boolean AllQuestionsAnswered() {
        int len = mQuestionBank.size();
        for (int i = 0; i < len; i++) {
            if (mQuestionBank.get(i).mUserAnswer == null) {
                return false;
            }
        }
        return true;
    }
}
