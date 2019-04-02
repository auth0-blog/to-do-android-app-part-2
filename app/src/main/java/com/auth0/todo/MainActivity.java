package com.auth0.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.todo.identity.AuthAwareActivity;
import com.auth0.todo.util.ToDoListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AuthAwareActivity implements Response.Listener<JSONArray>, Response.ErrorListener {
    private ToDoListAdapter toDoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create and configure the adapter
        this.toDoListAdapter = new ToDoListAdapter(this);
        ListView microPostsListView = findViewById(R.id.to_do_items);
        microPostsListView.setAdapter(toDoListAdapter);

        // issue the request
        String url = "http://10.0.2.2:3001";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest microPostsRequest = new JsonArrayRequest(url, this, this);
        queue.add(microPostsRequest);
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            List<ToDoItem> toDoItems = new ArrayList<>(response.length());
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);
                String id = item.getString("_id");
                String message = item.getString("message");

                toDoItems.add(new ToDoItem(id, message));
            }
            toDoListAdapter.setToDoList(toDoItems);
        } catch (JSONException error) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(error.toString())
                    .show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        toDoListAdapter.setToDoList(new ArrayList<ToDoItem>());
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error.getMessage())
                .show();
    }

    public void openToDoForm(View view) {
        if (authenticationHandler.hasValidCredentials()) {
            startActivity(new Intent(this, ToDoFormActivity.class));
        }
    }
}