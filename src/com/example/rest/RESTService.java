package com.example.rest;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class RESTService extends IntentService {

    private static final String TAG = "RESTService";

    public RESTService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent");

        Uri uri = intent.getData();
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        Log.v(TAG, "action:" + action);

        String url = buildUrl(uri, extras);

        Log.v(TAG, "urlString:" + url);
        HttpRequestBase request = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();

        String json = null;
        int status = 0;

        try {
            HttpResponse response = httpClient.execute(request);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine != null) {
                status = statusLine.getStatusCode();
                Log.v(TAG, "status:" + status);
            }

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    json = EntityUtils.toString(entity);
                    Log.v(TAG, "stringContent:" + json);
                }
            }

        } catch (ClientProtocolException ex) {
            Log.e(TAG, "Failed to ...", ex);
        } catch (IOException ex) {
            Log.e(TAG, "Failed to ...", ex);
        }

        Intent resultIntent = new Intent();
        resultIntent.setAction(action);
        resultIntent.putExtra("status", status);
        resultIntent.putExtra("json", json);

        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    private String buildUrl(Uri uri, Bundle extras) {
        Uri.Builder builder = uri.buildUpon();
        Bundle config = extras.getBundle("config");

        String path = config.getString("path");
        if (path != null) {
            builder.path(path);
        }

        Bundle params = config.getBundle("params");
        if (params != null) {
            for (String key : params.keySet()) {
                builder.appendQueryParameter(key, params.get(key).toString());
            }
        }
        return builder.build().toString();
    }
}