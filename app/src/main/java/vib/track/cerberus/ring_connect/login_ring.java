package vib.track.cerberus.ring_connect;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.data.LoginData;
import vib.track.cerberus.data.LoginResponse;
import vib.track.cerberus.data.RingLoginData;
import vib.track.cerberus.data.RingLoginResponse;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;
import vib.track.cerberus.ui.login_registration_activities.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.Logger;

public class login_ring extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private Button mSubmit;
    private ServiceApi service;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ring);

        mEmail = (EditText)findViewById(R.id.ringEmail);
        mPassword = (EditText)findViewById(R.id.ringPass);
        mSubmit = (Button)findViewById(R.id.ringSubmit);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        sharedpreferences = getSharedPreferences("CerberusPreferences", Context.MODE_PRIVATE);

    }

    public void submitRing(View view) {
        String email = mEmail.getText().toString();
        String pass = mPassword.getText().toString();

        RingLoginData data = new RingLoginData(email, pass);

        service.ringLogin(data).enqueue(new Callback<RingLoginResponse>() {
            @Override
            public void onResponse(Call<RingLoginResponse> call, Response<RingLoginResponse> response) {
                RingLoginResponse result = response.body();

                if (!result.getRestClient().isEmpty()) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("restClient", result.getRestClient());
                    editor.commit();
                    Intent auth = new Intent(login_ring.this, auth_ring.class);
                    startActivity(auth);
                } else {
                    Toast.makeText(login_ring.this, "Login error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RingLoginResponse> call, Throwable t) {
                Toast.makeText(login_ring.this, "Login error", Toast.LENGTH_SHORT).show();
                Log.e("Login error", t.getMessage());
            }
        });
    }
}