package com.oc.rss.dettemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Brandon and Maxime on 21/11/2016.
 */

public class AddActivity extends AppCompatActivity {

    ArrayList<Dette> arrayDettes;
    ArrayList<Dette> arrayDettesTmp = new ArrayList<Dette>();
    LinearLayout mylinearlayout;
    CustomAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mylinearlayout = (LinearLayout) findViewById(R.id.mylinearlayout);
        listView = (ListView) findViewById(R.id.listView);
        if (getIntent().getSerializableExtra("key") != null) {
            arrayDettes = (ArrayList<Dette>) getIntent().getSerializableExtra("key");
            for (int i = 0; i < arrayDettes.size(); i++) {
                //Does not display the debt when it is equal to zero
                if (Integer.parseInt(arrayDettes.get(i).sum) != 0) {
                    arrayDettesTmp.add(arrayDettes.get(i));
                }
            }
        }
        adapter = new CustomAdapter(AddActivity.this, arrayDettesTmp);
        listView.setAdapter(adapter);
    }
}
