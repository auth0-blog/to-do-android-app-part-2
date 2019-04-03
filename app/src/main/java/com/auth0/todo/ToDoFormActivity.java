package com.auth0.todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.todo.identity.AuthAwareActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;

public class ToDoFormActivity extends AuthAwareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_form);
    }

    public void addToDoItem(View view) {
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();

        try {
            final Context context = this;
            JSONObject newPost = new JSONObject();
            newPost.put("message", message);

            String url = "http://10.0.2.2:3001";
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, newPost,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Error")
                                    .setMessage(error.getMessage())
                                    .show();
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + authenticationHandler.getAccessToken());
                    return headers;
                }
            };

            // Add the request to the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(postRequest);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }
}