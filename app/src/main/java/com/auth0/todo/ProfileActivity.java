package com.auth0.todo;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.auth0.todo.identity.AuthAwareActivity;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AuthAwareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final ImageView profilePicture = this.findViewById(R.id.profile_picture);
        String picture = this.getIntent().getStringExtra("profilePicture");
        final TextView textView = this.findViewById(R.id.username);
        textView.setText(this.getIntent().getStringExtra("username"));
        Picasso.get().load(picture).into(profilePicture);
    }
}