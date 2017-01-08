package com.example.laurabravopriegue.petiquiz;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Quiz extends Fragment {
    FragmentActivity faActivity;
    LinearLayout llLayout;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Button mTrueButton;
    private Button mFalseButton;

    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView userText;
    private TextView userScore;

    private Question[] mQuestionBank = new Question[] {
                new Question("waiting for questions to load", true, null)
    };

    private int mCurrentIndex = 0;

    private void updateQuestion() {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        String question = currentQuestion.getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = (Button) llLayout.findViewById(R.id.true_button);
        mFalseButton = (Button) llLayout.findViewById(R.id.false_button);
        if (currentQuestion.mUserAnswer != null) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            Toast.makeText(super.getActivity(), "You already answered with "+currentQuestion.mUserAnswer + " to this question", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean userPressed) {
        Question currentQuestion = mQuestionBank[mCurrentIndex];
        if (currentQuestion.mUserAnswer != null) {
            Toast.makeText(super.getActivity(), "You've already answered this question!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        boolean answerIsTrue = currentQuestion.isAnswerTrue();
        int messageResId = 0;

        if (userPressed == answerIsTrue) {
            messageResId = R.string.correct_toast;

            SharedPreferences pref = super.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            boolean loggedIn = pref.getBoolean("loggedIn", false);
            if (loggedIn) {
                SharedPreferences.Editor editor = pref.edit();
                Integer score = pref.getInt("score", 0);
                score += 10;
                editor.putInt("score", score); // Storing integer
                editor.commit(); // commit changes

                //update score on screen
                userScore.setText(score.toString());
            }
        } else {
            messageResId = R.string.incorrect_toast;
        }
        currentQuestion.mUserAnswer = userPressed;
        Toast.makeText(super.getActivity(), messageResId, Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mCurrentIndex = arguments.getInt("question");
        }

        faActivity  = super.getActivity();
        llLayout    = (LinearLayout) inflater.inflate(R.layout.activity_quiz, container, false);

        mQuestionTextView = (TextView) llLayout.findViewById(R.id.question_text_view);
        userText = (TextView) llLayout.findViewById(R.id.userText);
        userScore = (TextView) llLayout.findViewById(R.id.userScore);

        SharedPreferences pref = super.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean loggedIn = pref.getBoolean("loggedIn", false);
        if (loggedIn) {
            String userName = pref.getString("username", "");
            Integer score = pref.getInt("score", 0);
            userText.setText(userName);
            userScore.setText(score.toString());
        }

        mTrueButton = (Button) llLayout.findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) llLayout.findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) llLayout.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        //updateQuestion();

        DownloadQuestions downloadQuestions = new DownloadQuestions();
        downloadQuestions.execute();

        // Don't use this method, it's handled by inflater.inflate() above :
        // setContentView(R.layout.activity_layout);

        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
        llLayout.findViewById(R.id.linLayout_quiz);
        // Instead of :
        // findViewById(R.id.someGuiElement);
        return llLayout; // We must return the loaded Layout
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    public void QuestionsLoaded(JSONArray response) throws JSONException {
        int len = response.length();
        Question[] questionsBank = new Question[len];
        for (int i = 0; i < len; i++) {
            JSONObject question = response.getJSONObject(i);
            Boolean answer;
            if (question.getInt("answer") == 0) {
                answer = false;
            }
            else {
                answer = true;
            }
            String questionText = question.getString("question");
            questionsBank[i] = new Question(questionText, answer, null);
        }
        mQuestionBank = questionsBank;
        updateQuestion();
    }

    public class DownloadQuestions extends AsyncTask<Void, Void, JSONArray>
    {
        @Override
        public JSONArray doInBackground(Void... params)
        {
            SharedPreferences pref = faActivity.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            boolean loggedIn = pref.getBoolean("loggedIn", false);
            String str;
            if (loggedIn) {
                int userId = pref.getInt("userId", 0);
                str="http://mfcfund.ml/petiapp/GetQuestionsWithAnswers.php"+"?userId="+userId;
            }
            else {
                str="http://mfcfund.ml/petiapp/GetQuestions.php";
            }
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