package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UserArea extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etUserName = (EditText) findViewById(R.id.etUserName);
        final EditText etAge = (EditText) findViewById(R.id.etAge);
        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        int age = intent.getIntExtra("age", -1);

        String message = "Welcome " + name + "!";
        welcomeMessage.setText(message);
        etUserName.setText(username);
        etAge.setText(age + "");


    }

    public void backtomenu(View view)
    {
        Intent intent = new Intent(UserArea.this, menu.class);
        startActivity(intent);
    }

    public void startgame(View view)
    {
        Intent intent = new Intent(UserArea.this, Quiz.class);
        startActivity(intent);
    }
}