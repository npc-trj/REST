package com.example.rest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.example.rest.model.Track;
import com.example.rest.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class ListActivity2 extends ListActivityBase {
    private static final String TAG = "ListActivity2";

    @Override
    public void fillList(String json) {
        ArrayList<Track> list = new ArrayList<Track>();
        try {
            JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject playlist = data.getJSONObject(i);
                Track tr = new Track();
                tr.id = playlist.getLong("id");
                tr.title = playlist.getString("title");

                list.add(tr);
            }
        } catch (JSONException ex) {
            Log.e(TAG, "Failed to parse JSON", ex);
        }

        setListAdapter(new ArrayAdapter<Track>(this, android.R.layout.simple_list_item_1, list));
    }

    @Override
    public void sendRequest() {
        Log.v(TAG, "getTracks");
        Intent intent = new Intent(this, RESTService.class);
        intent.setAction(action());
        intent.setData(Uri.parse(Constants.HOST));

        Bundle params = new Bundle();
        long id = getIntent().getLongExtra("id", 0);
        params.putString("filter", "{\"playlist\":" + id + "}");

        Bundle config = new Bundle();
        config.putString("path", "/api/trackpack/");
        config.putBundle("params", params);

        intent.putExtra("config", config);
        startService(intent);
    }

    @Override
    public String action() {
        return Constants.ACTIVITY_2_ACTION;
    }
}
