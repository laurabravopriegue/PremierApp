package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.content.SharedPreferences;
import android.widget.Button;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean loggedIn = pref.getBoolean("loggedIn", false);
        if (loggedIn) {
            Button loginButton = (Button) findViewById(R.id.button4);
            loginButton.setText("LOG OUT");
        }
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(menu.this, Quiz.class);
        startActivity(intent);
    }

    public void loginbutton(View view)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean loggedIn = pref.getBoolean("loggedIn", false);
        if (loggedIn) {
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("loggedIn", false);
            editor.putString("user", "");
            editor.putInt("score", 0);

            editor.commit();

            Button loginButton = (Button) findViewById(R.id.button4);
            loginButton.setText("LOG IN");
        }
        else {
            Intent intent = new Intent(menu.this, Login.class);
            startActivity(intent);
        }
    }


    public void holaquetal(View view)
    {
        Intent intent = new Intent(menu.this, seequestions.class);
        startActivity(intent);
    }

}
