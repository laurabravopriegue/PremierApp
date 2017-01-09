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
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        llLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_menu, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
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

                // Don't use this method, it's handled by inflater.inflate() above :
                // setContentView(R.layout.activity_layout);

                // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
                llLayout.findViewById(R.id.activity_menu);
        // Instead of :
        // findViewById(R.id.someGuiElement);
        return llLayout; // We must return the loaded Layout
    }

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
            // Reload questions
            Questions.questionsLoaded = false;

            Button loginButton = (Button) llLayout.findViewById(R.id.login);
            loginButton.setText("LOG IN");
        }
        else {
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
        Questions.questionsLoaded = false;
        SharedPreferences pref = faActivity.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("score", 0);
        editor.commit();

        Toast.makeText(super.getActivity(), "Quiz restarted", Toast.LENGTH_SHORT)
                .show();

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
