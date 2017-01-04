package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(menu.this, Quiz.class);
        startActivity(intent);
    }

    public void loginbutton(View view)
    {
        Intent intent = new Intent(menu.this, Login.class);
        startActivity(intent);
    }


    public void holaquetal(View view)
    {
        Intent intent = new Intent(menu.this, seequestions.class);
        startActivity(intent);
    }

}
