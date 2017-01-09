package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class menu extends Fragment {
    LinearLayout llLayout;
    FragmentActivity faActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        faActivity  = super.getActivity();
        llLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_menu, container, false);
        // Check from shared preferences if user is logged in
        SharedPreferences pref = faActivity.getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean loggedIn = pref.getBoolean("loggedIn", false);
        if (loggedIn) {
            Button loginButton = (Button) llLayout.findViewById(R.id.login);
            loginButton.setText("LOG OUT");
        }

        //add event listeners to buttons
        llLayout.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
        llLayout.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
        llLayout.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
        llLayout.findViewById(R.id.questions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
        llLayout.findViewById(R.id.ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
        llLayout.findViewById(R.id.multiplayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });

        llLayout.findViewById(R.id.activity_menu);
        return llLayout;
    }

    // Function to start quiz view fragment
    public void startQuiz(View view)
    {
        Quiz newFragment = new Quiz();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loginbutton(View view)
    {
        SharedPreferences pref = faActivity.getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean loggedIn = pref.getBoolean("loggedIn", false);
        if (loggedIn) {
            // If user was logged in when pressed the login/logout button,
            // log him/her out by setting values back in sharedpreference
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("loggedIn", false);
            editor.putString("user", "");
            editor.putString("username", "");
            editor.putInt("score", 0);
            editor.putString("name", "");
            editor.putInt("age", 0);
            editor.putInt("userId", 0);
            editor.putInt("maxscore", 0);

            editor.commit();
            // By setting this boolean to false, all questions will be reloaded next time user starts a quiz,
            // so we can make sure all his/her answers are deleted
            Questions.questionsLoaded = false;

            Button loginButton = (Button) llLayout.findViewById(R.id.login);
            loginButton.setText("LOG IN");
        }
        else {
            // No logged in user, so start login fragment
            Login newFragment = new Login();
            Bundle args = new Bundle();
            newFragment.setArguments(args);
            FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    public void seeQuestions(View view)
    {
        seequestions newFragment = new seequestions();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void seeRanking(View view)
    {
        RankingListViewFragment newFragment = new RankingListViewFragment();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void multiplayer(View view)
    {
        Multiplayer newFragment = new Multiplayer();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void restart() {
        // Reload questions, and set score back to zero to restart quiz
        Questions.questionsLoaded = false;
        SharedPreferences pref = faActivity.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("score", 0);
        editor.commit();

        Toast.makeText(super.getActivity(), "Quiz restarted", Toast.LENGTH_SHORT)
                .show();

        // Start quiz fragment
        Quiz newFragment = new Quiz();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void Click(View v) {
        switch (v.getId()) {
            case R.id.start:
                startQuiz(llLayout);
                break;
            case R.id.restart:
                restart();
                break;
            case R.id.login:
                loginbutton(llLayout);
                break;
            case R.id.questions:
                seeQuestions(llLayout);
                break;
            case R.id.ranking:
                seeRanking(llLayout);
                break;
            case R.id.multiplayer:
                multiplayer(llLayout);
                break;
        }
    }

}
