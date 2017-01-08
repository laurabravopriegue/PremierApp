package com.example.laurabravopriegue.petiquiz;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class RankingAdapter extends ArrayAdapter<Ranking> {
    Context context;
    public RankingAdapter(Context context, ArrayList<Ranking> rankings) {
        super(context, 0, rankings);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Ranking ranking = getItem(position);
        final int positionFinal = position;
        final ViewGroup parentFinal = parent;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_question, parent, false);
        }
        // Lookup view for data population
        TextView text = (TextView) convertView.findViewById(R.id.txtQuestion);
        // Populate the data into the template view using the data object
        text.setText(ranking.name + ": " + ranking.score);
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