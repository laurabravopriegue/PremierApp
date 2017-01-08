package com.example.laurabravopriegue.petiquiz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuestionAdapter extends ArrayAdapter<Question> {
    Context context;
    public QuestionAdapter(Context context, ArrayList<Question> questions) {
        super(context, 0, questions);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Question question = getItem(position);
        final int positionFinal = position;
        final ViewGroup parentFinal = parent;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_question, parent, false);
            // Define click listener for the ViewHolder's View.
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (question.mUserAnswer != null) {
                        Toast.makeText(context, "You already answered with "+question.mUserAnswer + " to this question", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        Quiz newFragment = new Quiz();
                        Bundle args = new Bundle();
                        args.putInt("question", positionFinal);
                        newFragment.setArguments(args);
                        FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, newFragment);
                        transaction.commit();
                    }
                }
            });
            if (question.mUserAnswer != null) {
                convertView.setBackgroundColor(Color.GRAY);
            }
        }
        // Lookup view for data population
        TextView text = (TextView) convertView.findViewById(R.id.txtQuestion);
        // Populate the data into the template view using the data object
        text.setText(question.getTextResId());
        text.setTextSize(20);

        // Return the completed view to render on screen
        return convertView;
    }

    @Override

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}