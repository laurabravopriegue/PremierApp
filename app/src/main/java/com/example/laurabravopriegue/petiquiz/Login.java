package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class Login extends Fragment {
    FragmentActivity faActivity;
    RelativeLayout rLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        faActivity = (FragmentActivity)super.getActivity();
        rLayout = (RelativeLayout)inflater.inflate(R.layout.activity_login, container, false);
        final EditText etUserName = (EditText) rLayout.findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) rLayout.findViewById(R.id.etPassword);

        final Button bLogin = (Button) rLayout.findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) rLayout.findViewById(R.id.tvResgisterHere);

        registerLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Register newFragment = new Register();
                Bundle args = new Bundle();
                newFragment.setArguments(args);
                FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String username = etUserName.getText().toString();
                final String password = etPassword.getText().toString();

                if (username.matches("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                    builder.setMessage("Please provide a username!");
                    builder.setNegativeButton("OK", null);
                    builder.create();
                    builder.show();
                }
                else if (password.matches("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                    builder.setMessage("Please provide a password!");
                    builder.setNegativeButton("OK", null);
                    builder.create();
                    builder.show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {

                                    String name = jsonResponse.getString("name");
                                    int age = jsonResponse.getInt("age");
                                    int userId = jsonResponse.getInt("userID");
                                    int maxScore = jsonResponse.getInt("maxscore");
                                    // Begin:
                                    // Save logged in user to use in other activites
                                    SharedPreferences pref = faActivity.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();

                                    editor.putBoolean("loggedIn", true);
                                    editor.putString("username", username);
                                    editor.putInt("score", 0);
                                    editor.putString("name", name);
                                    editor.putInt("age", age);
                                    editor.putInt("userId", userId);
                                    editor.putInt("maxscore", maxScore);

                                    editor.commit(); // commit changes

                                    // reload questions
                                    Questions.questionsLoaded = false;
                                    UserArea newFragment = new UserArea();
                                    Bundle args = new Bundle();
                                    newFragment.setArguments(args);
                                    FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, newFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                                    builder.setMessage("Log In Failed");
                                    builder.setNegativeButton("Retry", null);
                                    builder.create();
                                    builder.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    LoginRequest loginRequest= new LoginRequest(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(faActivity);
                    queue.add(loginRequest);
                }
            }

        });
        rLayout.findViewById(R.id.activity_login);
        return rLayout;
    }
}
