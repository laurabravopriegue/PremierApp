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


public class Questions {
    public static Question[] mQuestionBank;
    public static Boolean questionsLoaded = false;

    public static Question[] getQuestions() {
        if (questionsLoaded) {
            return mQuestionBank;
        }
        else {
            return null;
        }
    }
}
