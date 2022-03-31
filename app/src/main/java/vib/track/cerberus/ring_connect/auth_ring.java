package vib.track.cerberus.ring_connect;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.data.RingAuthData;
import vib.track.cerberus.data.RingAuthResponse;
import vib.track.cerberus.data.RingLoginData;
import vib.track.cerberus.data.RingLoginResponse;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class auth_ring extends AppCompatActivity {
    EditText m2faCode;
    Button mSubmit;
    private ServiceApi service;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_ring);

        m2faCode = (EditText) findViewById(R.id.tfa);
        mSubmit = (Button) findViewById(R.id.tfa_submit);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        sharedPreferences = getSharedPreferences("CerberusPreferences", Context.MODE_PRIVATE);
    }

    public void submitAuth(View view) {
        int userId = sharedPreferences.getInt("UserId", 0);
        String code = m2faCode.getText().toString();
        String email = sharedPreferences.getString("tempEmail", "");
        String password = sharedPreferences.getString("tempPass", "");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("tempEmail");
        editor.remove("tempPass");
        editor.commit();

        RingAuthData data = new RingAuthData(userId, code, email, password);

        service.ringAuth(data).enqueue(new Callback<RingAuthResponse>() {
            @Override
            public void onResponse(Call<RingAuthResponse> call, Response<RingAuthResponse> response) {
                RingAuthResponse result = response.body();

                if (result.getResultCode() == 200) {
                    Intent home = new Intent(auth_ring.this, HomepageActivity.class);
                    startActivity(home);
                } else {
                    Toast.makeText(auth_ring.this, "Ring connection error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RingAuthResponse> call, Throwable t) {
                Toast.makeText(auth_ring.this, "Ring error", Toast.LENGTH_SHORT).show();
                Log.e("Login error", t.getMessage());
            }
        });
    }
}