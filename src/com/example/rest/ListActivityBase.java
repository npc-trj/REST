package com.example.rest;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public abstract class ListActivityBase extends ListActivity {
    private ResponseReceiver mResponseReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResponseReceiver = new ResponseReceiver();

        LocalBroadcastManager.getInstance(this).registerReceiver(mResponseReceiver, new IntentFilter(action()));

        sendRequest();
    }

    @Override
    public void onDestroy() {
        if (mResponseReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mResponseReceiver);
            mResponseReceiver = null;
        }
        super.onDestroy();
    }

    public abstract void fillList(String json);

    public abstract void sendRequest();

    public abstract String action();

    private class ResponseReceiver extends BroadcastReceiver {
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String json = intent.getStringExtra("json");
            if (json != null) {
                fillList(json);
            }
        }
    }
}
