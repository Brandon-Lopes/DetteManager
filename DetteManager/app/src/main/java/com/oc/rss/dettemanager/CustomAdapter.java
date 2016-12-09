package com.oc.rss.dettemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Brandon and Maxime on 24/11/2016.
 */

public class CustomAdapter extends ArrayAdapter<Dette> {

    public CustomAdapter(Context context, ArrayList<Dette> dettes) {

        super(context, 0, dettes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedSettings = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Get the data item for this position
        Dette dette = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }
        // Lookup view for data population
        TextView tvFirstName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvSum = (TextView) convertView.findViewById(R.id.tvSum);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        tvFirstName.setText(dette.firstname + " " + dette.lastname.toUpperCase());

        int i = 0;
        if (Integer.parseInt(dette.sum) < 0) {
            tvSum.setTextColor(Color.parseColor(sharedSettings.getString("Color_1", getContext().getResources().getString(R.string.Default_String1)))); //color red
            //avoiding the display of the minus character
            i = -Integer.parseInt(dette.sum);
            tvSum.setText(i + "€");
        } else if (Integer.parseInt(dette.sum) > 0) {
            tvSum.setTextColor(Color.parseColor(sharedSettings.getString("Color_2", getContext().getResources().getString(R.string.Default_String2)))); // color green
            tvSum.setText(dette.sum + "€");
        }
        tvDescription.setText(dette.description);
        // Return the completed view to render on screen
        return convertView;
    }
}

