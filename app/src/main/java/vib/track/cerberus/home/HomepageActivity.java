package vib.track.cerberus.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.bluetooth.BluetoothHandler;
import vib.track.cerberus.data.RingNotifyData;
import vib.track.cerberus.data.RingNotifyResponse;
import vib.track.cerberus.databinding.ActivityHomepageBinding;
import vib.track.cerberus.history.HistoryList;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;
import vib.track.cerberus.ring_connect.auth_ring;

public class HomepageActivity extends AppCompatActivity {

    Button B_toHistory, B_toSettings;
    private ServiceApi service;
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        B_toHistory = findViewById(R.id.buttonHistory);
        B_toSettings = findViewById(R.id.buttonSettings);
        BluetoothHandler.init(this);
        B_toHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HistoryList());
            }
        });

        B_toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SecondFragment());
            }
        });

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sharedPreferences = getSharedPreferences("CerberusPreferences", Context.MODE_PRIVATE);
        BluetoothHandler.scan(this);
        registerRing();
    }
    private  void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ftran = fm.beginTransaction();
        ftran.replace(R.id.HomeFrame_FragView, fragment);
        ftran.commit();
    }

    private void registerRing() {
        boolean registeredRing = sharedPreferences.getBoolean("registeredRing", false);
        if (registeredRing == false) {
            int userId = sharedPreferences.getInt("UserId", 0);
            RingNotifyData rData = new RingNotifyData(userId);
            service.ringNotify(rData).enqueue(new Callback<RingNotifyResponse>() {
                @Override
                public void onResponse(Call<RingNotifyResponse> rCall, Response<RingNotifyResponse> rResponse) {
                    RingNotifyResponse rResult = rResponse.body();
                    if (rResult.getResultCode() == 200) {
                        Intent home = new Intent(HomepageActivity.this, HomepageActivity.class);
                        startActivity(home);
                    } else {
                        Toast.makeText(HomepageActivity.this, "Ring notify error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RingNotifyResponse> rCall, Throwable t) {
                    Toast.makeText(HomepageActivity.this, "Ring Notify Error", Toast.LENGTH_SHORT).show();
                    Log.e("Ring Notify Error", t.getMessage());
                }
            });
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("registeredRing", true);
            editor.commit();
        }

    }
}