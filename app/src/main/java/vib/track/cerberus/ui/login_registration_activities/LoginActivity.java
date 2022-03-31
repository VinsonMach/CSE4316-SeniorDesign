package vib.track.cerberus.ui.login_registration_activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vib.track.cerberus.R;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;
import vib.track.cerberus.data.LoginData;
import vib.track.cerberus.data.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.ring_connect.login_ring;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mEmailLoginButton;
    private Button mJoinButton;
    private ProgressBar mProgressView;
    private ServiceApi service;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mEmailLoginButton = (Button) findViewById(R.id.login_button);
        mJoinButton = (Button) findViewById(R.id.join_button);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        sharedpreferences = getSharedPreferences("CerberusPreferences", Context.MODE_PRIVATE);

        mEmailLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mJoinButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Checking password validation
        if (password.isEmpty()) {
            mEmailView.setError("Please enter your password.");
            focusView = mEmailView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("Please enter more than 6 characters.");
            focusView = mPasswordView;
            cancel = true;
        }

        // Checking email validation
        if (email.isEmpty()) {
            mEmailView.setError("Please enter your email.");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Please enter an valid email including @.");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLogin(new LoginData(email, password));
            showProgress(true);
        }
    }

    private void startLogin(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                //Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("UserId", result.getUserId());
                editor.commit();

                if (result.isRefreshToken()) {
                    Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), login_ring.class);
                    startActivity(intent);
                }

                showProgress(false);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                Log.e("Login error", t.getMessage());
                Log.e("Login stack trace", t.getStackTrace().toString());
                showProgress(false);
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}



