package com.oc.rss.dettemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Dette> dettes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //declaration of my buttons
        Button detailButton1 = (Button) findViewById(R.id.buttonDetails1);
        Button detailButton2 = (Button) findViewById(R.id.buttonDetails2);

        //add action listener to my buttons
        detailButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent1 = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent1.putExtra("key", dettes);
                startActivity(detailIntent1);
            }
        });

        detailButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent2 = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent2.putExtra("key2", dettes);
                startActivity(detailIntent2);
            }
        });
        new MyAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Toast.makeText(this, getResources().getString(R.string.Refresh), Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                break;

            case R.id.menu_settings:
                Intent IntentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(IntentSettings);
                break;

            case R.id.show_all:
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                addIntent.putExtra("key", dettes);
                startActivity(addIntent);
                break;

            case R.id.about:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage(getResources().getString(R.string.WarningBox));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        getResources().getString(R.string.Yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent_about = new Intent(Intent.ACTION_VIEW);
                                intent_about.setData(Uri.parse(getResources().getString(R.string.Portfolio)));
                                startActivity(intent_about);
                            }
                        });

                builder1.setNegativeButton(
                        getResources().getString(R.string.No),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;

            case R.id.exit:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    class MyAsyncTask extends AsyncTask<String, Void, ArrayList<Dette>> {
        protected void onPreExecute() {

        }

        protected ArrayList<Dette> doInBackground(String... params) {
            ArrayList<Dette> dettes = new HttpHandler(MainActivity.this).makeServiceCall("http://serveur.96.lt/dettes.json");
            return dettes;
        }

        protected void onPostExecute(ArrayList<Dette> result) {
            SharedPreferences sharedSettings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            int val = 0, val2 = 0;
            int j, i = 0;
            for (Dette d : result) {
                if ((i = Integer.parseInt(d.sum)) > 0) val += i;
                if ((j = Integer.parseInt(d.sum)) < 0) val2 += j;
            }
            TextView mtvIOT = (TextView) findViewById(R.id.textView2);
            TextView mtvTOM = (TextView) findViewById(R.id.textView3);

            mtvIOT.setText(getResources().getString(R.string.I_owe_them) + " " + val + "€");
            mtvTOM.setText(getResources().getString(R.string.They_owe_me) + " " + (-val2) + "€");
            mtvIOT.setTextColor(Color.parseColor(sharedSettings.getString("Color_2", getResources().getString(R.string.Default_String2))));
            mtvTOM.setTextColor(Color.parseColor(sharedSettings.getString("Color_1", getResources().getString(R.string.Default_String1))));

            dettes = result;
        }
    }

}
