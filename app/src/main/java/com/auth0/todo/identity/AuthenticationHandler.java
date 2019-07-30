package com.auth0.todo.identity;

import android.app.Dialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.auth0.android.Auth0;
import com.auth0.android.Auth0Exception;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.storage.CredentialsManagerException;
import com.auth0.android.authentication.storage.SecureCredentialsManager;
import com.auth0.android.authentication.storage.SharedPreferencesStorage;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.provider.AuthCallback;
import com.auth0.android.provider.VoidCallback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.auth0.android.result.UserProfile;
import com.auth0.todo.MainActivity;
import com.auth0.todo.ProfileActivity;
import com.auth0.todo.R;

public class AuthenticationHandler implements AuthCallback, BaseCallback<Credentials, CredentialsManagerException> {
    private Auth0 auth0;
    private AuthenticationAPIClient authenticationAPIClient;
    private AuthAwareActivity originalActivity;
    private SecureCredentialsManager credentialsManager;
    private Credentials credentials;

    AuthenticationHandler(AuthAwareActivity originalActivity) {
        this.originalActivity = originalActivity;

        // configuring Auth0
        auth0 = new Auth0(originalActivity);
        auth0.setOIDCConformant(true);
        AuthenticationAPIClient client = new AuthenticationAPIClient(auth0);
        credentialsManager = new SecureCredentialsManager(originalActivity, client, new SharedPreferencesStorage(originalActivity));
    }

    public void startAuthenticationProcess() {
        WebAuthProvider.init(auth0)
                .withScheme("to-do")
                .withScope("openid profile email offline_access read:current_user")
                .withAudience(String.format("https://%s/api/v2/", originalActivity.getString(R.string.com_auth0_domain)))
                .start(originalActivity, this);
    }

    public void logout() {
        WebAuthProvider.logout(auth0)
                .withScheme("to-do")
                .start(originalActivity, new VoidCallback() {
            @Override
            public void onSuccess(Void payload) {
                credentialsManager.clearCredentials();
                originalActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "See you soon!";
                        Toast.makeText(originalActivity, message, Toast.LENGTH_SHORT).show();

                        if (originalActivity instanceof MainActivity) return;
                        originalActivity.startActivity(new Intent(originalActivity, MainActivity.class));
                    }
                });
            }

            @Override
            public void onFailure(final Auth0Exception error) {
                // Log out canceled, keep the user logged in
                originalActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = error.getMessage();
                        Toast.makeText(originalActivity, message, Toast.LENGTH_SHORT).show();

                        if (originalActivity instanceof MainActivity) return;
                        originalActivity.startActivity(new Intent(originalActivity, MainActivity.class));
                    }
                });
            }
        });
    }

    public String getAccessToken() {
        return credentials.getAccessToken();
    }

    @Override
    public void onFailure(@NonNull final Dialog dialog) {
        originalActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    @Override
    public void onFailure(AuthenticationException exception) {
        new AlertDialog.Builder(originalActivity)
                .setTitle("Authentication Error")
                .setMessage(exception.getMessage())
                .show();
    }

    @Override
    public void onSuccess(@NonNull Credentials credentials) {
        credentialsManager.saveCredentials(credentials);
        this.credentials = credentials;
        this.authenticationAPIClient = new AuthenticationAPIClient(auth0);
        String accessToken = this.credentials.getAccessToken();
        final UsersAPIClient usersClient = new UsersAPIClient(auth0, accessToken);
        this.authenticationAPIClient.userInfo(accessToken)
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(UserProfile userInfo) {
                        usersClient.getProfile(userInfo.getId())
                                .start(new BaseCallback<UserProfile, ManagementException>() {
                                    @Override
                                    public void onSuccess(UserProfile profile) {
                                        Intent profileIntent = new Intent(originalActivity, ProfileActivity.class);
                                        profileIntent.putExtra("profilePicture", profile.getPictureURL());
                                        profileIntent.putExtra("username", profile.getEmail());
                                        profileIntent.putExtra("name", (String) profile.getUserMetadata().get("name"));
                                        profileIntent.putExtra("birthdate", (String) profile.getUserMetadata().get("birthdate"));
                                        originalActivity.startActivity(profileIntent);
                                    }

                                    @Override
                                    public void onFailure(ManagementException error) {
                                        new AlertDialog.Builder(originalActivity)
                                                .setTitle("Authentication Error")
                                                .setMessage(error.getMessage())
                                                .show();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        new AlertDialog.Builder(originalActivity)
                                .setTitle("Authentication Error")
                                .setMessage(error.getMessage())
                                .show();
                    }
                });
    }

    @Override
    public void onFailure(CredentialsManagerException error) {
        startAuthenticationProcess();
    }
}