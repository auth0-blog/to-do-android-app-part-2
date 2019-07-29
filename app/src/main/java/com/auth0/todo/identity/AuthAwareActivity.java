package com.auth0.todo.identity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AuthAwareActivity extends AppCompatActivity {
    protected AuthenticationHandler authenticationHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.authenticationHandler = new AuthenticationHandler(this);

    }
}