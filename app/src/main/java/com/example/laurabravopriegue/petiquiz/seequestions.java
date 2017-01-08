package com.example.laurabravopriegue.petiquiz;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class seequestions extends ListFragment {
    FragmentActivity faActivity;
    RelativeLayout llLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        faActivity = super.getActivity();
        llLayout = (RelativeLayout) inflater.inflate(R.layout.activity_seequestions, container, false);

        if (!Questions.questionsLoaded) {
            seequestions.DownloadQuestions downloadQuestions = new seequestions.DownloadQuestions();
            downloadQuestions.execute();
        }

        llLayout.findViewById(R.id.linLayout_quiz);
        return llLayout;
    }


    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        if (Questions.questionsLoaded) {
            // Construct the data source
            ArrayList<Question> arrayOfQuestions = Questions.mQuestionBank;
            // Create the adapter to convert the array to views
            QuestionAdapter adapter = new QuestionAdapter(faActivity, arrayOfQuestions);
            // Attach the adapter to a ListView
            getListView().setAdapter(adapter);
        }
    }

    public void QuestionsLoaded(JSONArray response) throws JSONException {
        int len = response.length();
        ArrayList<Question> questionsBank = new ArrayList<Question>();
        for (int i = 0; i < len; i++) {
            JSONObject question = response.getJSONObject(i);
            Boolean answer;
            Integer userAnswer = null;
            if (question.getInt("answer") == 0) {
                answer = false;
            }
            else {
                answer = true;
            }
            if (!question.isNull("userAnswer")) {
                userAnswer = question.getInt("userAnswer");
            }
            String questionText = question.getString("question");
            String explanationText = question.getString("explanation");
            questionsBank.add(i, new Question(questionText, answer, userAnswer, explanationText));
        }
        Questions.mQuestionBank = questionsBank;
        Questions.questionsLoaded = true;
        // Construct the data source
        ArrayList<Question> arrayOfQuestions = Questions.mQuestionBank;
        // Create the adapter to convert the array to views
        QuestionAdapter adapter = new QuestionAdapter(faActivity, arrayOfQuestions);
        // Attach the adapter to a ListView
        getListView().setAdapter(adapter);
    }

    public class DownloadQuestions extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        public JSONArray doInBackground(Void... params)
        {
            SharedPreferences pref = faActivity.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            boolean loggedIn = pref.getBoolean("loggedIn", false);
            String str;
            /* This can be used if we want to store all user answers online
            if (loggedIn) {
                int userId = pref.getInt("userId", 0);
                str="http://mfcfund.ml/petiapp/GetQuestionsWithAnswers.php"+"?userId="+userId;
            }
            else {
                str="http://mfcfund.ml/petiapp/GetQuestions.php";
            }*/
            str="http://mfcfund.ml/petiapp/GetQuestions.php";
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                return new JSONArray(stringBuffer.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onPostExecute(JSONArray response)
        {
            if(response != null)
            {
                try {
                    QuestionsLoaded(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
