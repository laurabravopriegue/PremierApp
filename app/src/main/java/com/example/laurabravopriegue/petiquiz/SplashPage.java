package com.example.laurabravopriegue.petiquiz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashPage.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

        //Delete old score of logged in player
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("score", 0);
        editor.commit();
        //force reload questions
        Questions.questionsLoaded = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}


