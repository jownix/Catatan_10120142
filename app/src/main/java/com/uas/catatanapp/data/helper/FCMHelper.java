/**
 *   NIM     : 10120142
 *   Nama    : Jhonathan Kenzo
 *   Kelas   : IF4
 */
package com.uas.catatanapp.data.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FCMHelper {
    public static String token = "keren";
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";


    public static String getToken(Context ctx) {
        String TAG = "GGWP";

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();
                });

        return token;
    }

    public static void sendNotifNewNote(String token, String title, String category) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                JSONObject jsonNotif = new JSONObject();
                JSONObject wholeObj = new JSONObject();

                try {
                    jsonNotif.put("title", "New note " + title);
                    jsonNotif.put("body", "Category " + category + " with title " + title);
                    wholeObj.put("to", token);
                    wholeObj.put("notification", jsonNotif);

                } catch (JSONException e) {
                    Log.d("FCM ERROR", e.toString());
                }

                RequestBody rBody = RequestBody.create(wholeObj.toString(), mediaType);
                Request request = new Request.Builder()
                        .url(FCM_MESSAGE_URL)
                        .post(rBody)
                        .addHeader("Authorization", "key=" + com.uas.catatanapp.data.token.getFCM_SERVER_KEY())
                        .addHeader("Content-Type", "application/json")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    Log.d("GGWP", response.toString());
                } catch (IOException e) {
                    Log.d("FCM ERROR", e.toString());
                }

                return null;
            }
        }.execute();
    }
}

