package com.auth0.todo.util;

import android.view.View;
import android.widget.Button;

import com.auth0.todo.identity.AuthenticationHandler;

public class LoginListener implements Button.OnClickListener {
    private AuthenticationHandler authenticationHandler;

    public LoginListener(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    public void onClick(View v) {
        authenticationHandler.startAuthenticationProcess();
    }
}