package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserArea extends Fragment {
    FragmentActivity    faActivity;
    RelativeLayout rLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        faActivity = (FragmentActivity)    super.getActivity();
        rLayout = (RelativeLayout)    inflater.inflate(R.layout.activity_user_area, container, false);

        final EditText etUserName = (EditText) rLayout.findViewById(R.id.etUserName);
        final EditText etAge = (EditText) rLayout.findViewById(R.id.etAge);
        final TextView welcomeMessage = (TextView) rLayout.findViewById(R.id.tvWelcomeMsg);

        SharedPreferences pref = super.getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String username = pref.getString("username", "");
        Integer age = pref.getInt("age", 0);
        String name = pref.getString("name", "");

        String message = "Welcome " + name + "!";
        welcomeMessage.setText(message);
        etUserName.setText(username);
        etAge.setText(age + "");

        rLayout.findViewById(R.id.startGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });
        rLayout.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(view);
            }
        });

        rLayout.findViewById(R.id.activity_user_area);
        return rLayout;
    }

    public void backtomenu()
    {
        menu newFragment = new menu();
        Bundle args = new Bundle();
        newFragment.setArguments(args);
        FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void startgame()
    {
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
            case R.id.home:
                backtomenu();
                break;
            case R.id.startGame:
                startgame();
                break;
        }
    }
}