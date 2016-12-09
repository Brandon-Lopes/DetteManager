package com.oc.rss.dettemanager;

/**
 * Created by Brandon and Maxime on 24/11/2016.
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    private Context context;

    public HttpHandler(Context context) {
        this.context = context;
    }

    public ArrayList<Dette> makeServiceCall(String reqUrl) {
        String response = null;
        JSONArray o;
        ArrayList<Dette> dettes = new ArrayList<>();
        //check internet connexion
        if (isOnline(context)) {
            try {
                URL url = new URL(reqUrl);
                HttpURLConnection httpURLConnection;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection == null) {
                    return null;
                }
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    try {
                        String inputLine;
                        while ((inputLine = bufferedReader.readLine()) != null) {
                            stringBuffer.append(inputLine).append("\n");
                        }
                    } finally {
                        bufferedReader.close();
                        notification(R.mipmap.ic_launcher, context.getString(R.string.Notification_title_online), context.getString(R.string.Notification_content_online));
                    }
                    o = new JSONArray(stringBuffer.toString());
                    response = stringBuffer.toString();
                    //Writting file
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(response);
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        } else {
            response = readFromFile(context);
            notification(R.mipmap.ic_launcher, context.getString(R.string.Notification_title_offline), context.getString(R.string.Notification_content_offline));
        }
        if (response != null) {
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    String firstname = c.getString("firstname");
                    String lastname = c.getString("lastname");
                    String sum = c.getString("sum");
                    String description = c.getString("description");

                    Dette dette = new Dette();

                    dette.firstname = firstname;
                    dette.lastname = lastname;
                    dette.sum = sum;
                    dette.description = description;

                    dettes.add(dette);
                }
            } catch (final JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return dettes;
    }

    private String readFromFile(Context context) {
        //Initialisation in case the user has not internet at the first launch
        String ret = "[\n" +
                "  {\n" +
                "    \"firstname\": \"\",\n" +
                "    \"lastname\": \"\",\n" +
                "    \"sum\": 0,\n" +
                "    \"description\": \"\"\n" +
                "  }\n" +
                "]";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void notification(int icon, String title, String content) {
        //Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}

