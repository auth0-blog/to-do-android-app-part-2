package com.auth0.todo;

import android.os.Bundle;
import android.widget.Button;

import com.auth0.todo.identity.AuthAwareActivity;
import com.auth0.todo.util.LoginListener;

public class MainActivity extends AuthAwareActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginBtn = this.findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new LoginListener(authenticationHandler));
    }
}