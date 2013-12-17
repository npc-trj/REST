package com.example.rest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.rest.model.Playlist;
import com.example.rest.utils.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class ListActivity1 extends ListActivityBase {
    private static final String TAG = "ListActivity1";

    @Override
    public void fillList(String json) {
        ArrayList<Playlist> list = new ArrayList<Playlist>();
        try {
            JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject playlist = data.getJSONObject(i);
                Playlist pl = new Playlist();
                pl.id = playlist.getLong("id");
                pl.title = playlist.getString("title");

                list.add(pl);
            }
        } catch (JSONException ex) {
            Log.e(TAG, "Failed to parse JSON", ex);
        }

        setListAdapter(new ArrayAdapter<Playlist>(this, android.R.layout.simple_list_item_1, list));
    }

    @Override
    public void sendRequest() {
        Log.v(TAG, "getPlaylists");
        Intent intent = new Intent(this, RESTService.class);
        intent.setAction(action());
        intent.setData(Uri.parse(Constants.HOST));
        Bundle config = new Bundle();
        config.putString("path", "/api/playlist/");
        intent.putExtra("config", config);
        startService(intent);
    }

    @Override
    public String action() {
        return Constants.ACTIVITY_1_ACTION;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Playlist pl = (Playlist) getListAdapter().getItem(position);
        Log.v(TAG, "playlist:" + pl);

        Intent intent = new Intent(this, ListActivity2.class);
        // It's better to make our model classes Serializable or Parcelable (this just for example)
        intent.putExtra("id", pl.id);
        startActivity(intent);
    }
}
