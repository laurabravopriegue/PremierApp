package com.example.laurabravopriegue.petiquiz;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class Register extends Fragment {
    FragmentActivity faActivity;
    RelativeLayout rLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        rLayout    = (RelativeLayout)    inflater.inflate(R.layout.activity_register, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        final EditText etAge = (EditText) rLayout.findViewById(R.id.etAge);
        final EditText etName = (EditText) rLayout.findViewById(R.id.etName);
        final EditText etUserName = (EditText) rLayout.findViewById(R.id.etUserName);
        final EditText etPassword = (EditText) rLayout.findViewById(R.id.etPassword);
        final Button bRegister = (Button) rLayout.findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = etName.getText().toString();
                final String username = etUserName.getText().toString();
                final String password = etPassword.getText().toString();

                if (name.matches(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                    builder.setMessage("Please provide a name!");
                    builder.setNegativeButton("OK", null);
                    builder.create();
                    builder.show();
                }
                else if (username.matches(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                    builder.setMessage("Please provide a username!");
                    builder.setNegativeButton("OK", null);
                    builder.create();
                    builder.show();
                }
                else if (password.matches(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                    builder.setMessage("Please provide a password!");
                    builder.setNegativeButton("OK", null);
                    builder.create();
                    builder.show();
                }
                else if (etAge.getText().toString().matches(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                    builder.setMessage("Please provide your age!");
                    builder.setNegativeButton("OK", null);
                    builder.create();
                    builder.show();
                }
                else
                {
                    final int age = Integer.parseInt(etAge.getText().toString());
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Login newFragment = new Login();
                                    Bundle args = new Bundle();
                                    newFragment.setArguments(args);
                                    FragmentTransaction transaction = faActivity.getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, newFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(faActivity);
                                    builder.setMessage("Username already taken");
                                    builder.setNegativeButton("Change it", null);
                                    builder.create();
                                    builder.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(name, username, age, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(faActivity);
                    queue.add(registerRequest);
                }
            }
        });
        rLayout.findViewById(R.id.activity_register);
        return rLayout;
    }
}

