package com.example.laurabravopriegue.petiquiz;

import android.icu.text.AlphabeticIndex;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Quiz extends Fragment {
    FragmentActivity faActivity;
    LinearLayout llLayout;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mSkipButton;
    private Button mCheatButton;
    private Button mQuestionsButton;

    private Button mNextButton;
    private TextView mQuestionTextView;
    private TextView userText;
    private TextView userScore;

    private int mCurrentIndex = 0;

    private void updateQuestion() {
        Question currentQuestion = Questions.mQuestionBank.get(mCurrentIndex);
        String question = currentQuestion.getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = (Button) llLayout.findViewById(R.id.true_button);
        mFalseButton = (Button) llLayout.findViewById(R.id.false_button);
        mSkipButton = (Button) llLayout.findViewById(R.id.skip_button);
        mCheatButton = (Button) llLayout.findViewById(R.id.cheat_button);
        mQuestionsButton = (Button) llLayout.findViewById(R.id.questions_button);

        if (currentQuestion.mUserAnswer != null) {
            DisableButtons();
            Toast.makeText(super.getActivity(), "You've already answered this question!", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
            mSkipButton.setEnabled(true);
            mCheatButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean userPressed) {
        Question currentQuestion = Questions.mQuestionBank.get(mCurrentIndex);
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
            SharedPreferences.Editor editor = pref.edit();
            if (loggedIn) {
                Integer score = pref.getInt("score", 0);
                score += 10;
                editor.putInt("score", score); // Storing integer
                editor.commit(); // commit changes

                //update score on screen
                userScore.setText(score.toString());
            }
            else {
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
        if (userPressed) {
            currentQuestion.mUserAnswer = 1;
        }
        else {
            currentQuestion.mUserAnswer = 0;
        }
        Toast.makeText(super.getActivity(), messageResId, Toast.LENGTH_SHORT)
                .show();
        DisableButtons();

        if (Questions.AllQuestionsAnswered()) {
            SharedPreferences pref = super.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            boolean loggedIn = pref.getBoolean("loggedIn", false);
            if (loggedIn) {
                RecordScore();
            }
            else {
                int score = pref.getInt("score", 0);
                Toast.makeText(super.getActivity(), "You scored "+score+" points! Login to save your results.", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void skipAnswer() {
        Question currentQuestion = Questions.mQuestionBank.get(mCurrentIndex);
        if (currentQuestion.mUserAnswer != null) {
            Toast.makeText(super.getActivity(), "You've already answered this question!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        currentQuestion.mUserAnswer = 2;
        Toast.makeText(super.getActivity(), "Skipped question", Toast.LENGTH_SHORT)
                .show();

        if (Questions.AllQuestionsAnswered()) {
            SharedPreferences pref = super.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            boolean loggedIn = pref.getBoolean("loggedIn", false);
            if (loggedIn) {
                RecordScore();
            }
            else {
                int score = pref.getInt("score", 0);
                Toast.makeText(super.getActivity(), "You scored "+score+" points! Login to save your results.", Toast.LENGTH_SHORT)
                        .show();
            }
            DisableButtons();
        }
        else {
            mNextButton.performClick();
        }
    }

    private void cheatAnswer() {
        Question currentQuestion = Questions.mQuestionBank.get(mCurrentIndex);
        if (currentQuestion.mUserAnswer != null) {
            Toast.makeText(super.getActivity(), "You've already answered this question!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        currentQuestion.mUserAnswer = 3;
        Toast.makeText(super.getActivity(), currentQuestion.isAnswerTrue()+": "+currentQuestion.getExplanation(), Toast.LENGTH_SHORT)
                .show();
        DisableButtons();

        if (Questions.AllQuestionsAnswered()) {
            SharedPreferences pref = super.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            boolean loggedIn = pref.getBoolean("loggedIn", false);
            if (loggedIn) {
                RecordScore();
            }
            else {
                int score = pref.getInt("score", 0);
                Toast.makeText(super.getActivity(), "You scored "+score+" points! Login to save your results.", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void questions() {
        seequestions newFragment = new seequestions();
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    private void DisableButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        mSkipButton.setEnabled(false);
        mCheatButton.setEnabled(false);
    }

    private void RecordScore() {
        SharedPreferences pref = faActivity.getSharedPreferences("MyPref", 0); // 0 - for private mode
        int maxScore = pref.getInt("maxscore", 0);
        int score = pref.getInt("score", 0);
        int userid = pref.getInt("userId", 0);
        Toast.makeText(super.getActivity(), "You scored "+score+" points!", Toast.LENGTH_SHORT)
                .show();
        if (score > maxScore) {
            Toast.makeText(super.getActivity(), "It's a new high score for you! Sending it to the server...", Toast.LENGTH_SHORT)
                    .show();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {

                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ScoreRequest scoreRequest = new ScoreRequest(userid, score, responseListener);
            RequestQueue queue = Volley.newRequestQueue(faActivity);
            queue.add(scoreRequest);
        }
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
        else {
            userText.setText("Guest player");
            userScore.setText(Integer.toString(0));
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

        mSkipButton = (Button) llLayout.findViewById(R.id.skip_button);
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipAnswer();
            }
        });

        mCheatButton = (Button) llLayout.findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheatAnswer();
            }
        });

        mQuestionsButton = (Button) llLayout.findViewById(R.id.questions_button);
        mQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questions();
            }
        });

        mNextButton = (Button) llLayout.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % Questions.mQuestionBank.size();
                updateQuestion();
            }
        });


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        if (!Questions.questionsLoaded) {
            DownloadQuestions downloadQuestions = new DownloadQuestions();
            downloadQuestions.execute();
        }
        else {
            updateQuestion();
        }

        llLayout.findViewById(R.id.linLayout_quiz);
        return llLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
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
        //mQuestionBank = questionsBank;
        Questions.mQuestionBank = questionsBank;
        Questions.questionsLoaded = true;
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