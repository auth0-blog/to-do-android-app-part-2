package com.auth0.todo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.auth0.todo.identity.AuthAwareActivity;
import com.auth0.todo.util.LogoutListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AuthAwareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final ImageView profilePicture = this.findViewById(R.id.profile_picture);
        String picture = this.getIntent().getStringExtra("profilePicture");
        Picasso.get().load(picture).into(profilePicture);

        final TextView textView = this.findViewById(R.id.username);
        textView.setText(this.getIntent().getStringExtra("username"));

        final TextView nameTextView = this.findViewById(R.id.name);
        nameTextView.setText(this.getIntent().getStringExtra("name"));

        final TextView birthdateTextView = this.findViewById(R.id.birthdate);
        birthdateTextView.setText(this.getIntent().getStringExtra("birthdate"));

        final Button logoutBtn = this.findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(new LogoutListener(authenticationHandler));
    }
}