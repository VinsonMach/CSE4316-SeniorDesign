package vib.track.cerberus.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.bluetooth.BluetoothHandler;
import vib.track.cerberus.data.RingNotifyData;
import vib.track.cerberus.data.RingNotifyResponse;
import vib.track.cerberus.history.HistoryActivity;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;

public class HomepageActivity extends AppCompatActivity {

    private ServiceApi service;
    SharedPreferences sharedPreferences;

    DrawerLayout drawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // assign variables
        drawerLayout = findViewById(R.id.drawer_layout);
        BluetoothHandler.init(this);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sharedPreferences = getSharedPreferences("CerberusPreferences", Context.MODE_PRIVATE);
        BluetoothHandler.scan(this);
        registerRing();
    }

    public void clickMenu(View view){
        // open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawer) {
        // open drawer layout
        drawer.openDrawer(GravityCompat.START);
    }

    public void clickLogo(View view){
        // close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawer) {
        // close drawer layout
        // check condition
        if (drawer.isDrawerOpen(GravityCompat.START)){
            // close drawer when drawer is open
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void clickHistory(View view){
        // recreate history activity
        redirectActivity(this, HistoryActivity.class);
    }

    public void clickSettings(View view){
        // redirect activity to Settings
        redirectActivity(this,SecondFragment.class);
    }

    public void clickCredits(View view){
        // close app
        redirectActivity(this,credits.class);
    }

    public void clickLogout(View view){
        // close app
        logout(this);
    }

    public static void logout(Activity activity) {
        // initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // set title
        builder.setTitle("Logout");
        // set message
        builder.setMessage("Are you sure that you want to logout?");
        // positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences shP = activity.getSharedPreferences("CerberusPreferences", Context.MODE_PRIVATE);
                shP.edit().clear().commit();
                SharedPreferences sharedP = activity.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                sharedP.edit().clear().commit();
                // finish activity
                activity.finishAffinity();
                // exit app
                System.exit(0);
            }
        });
        // negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        
        // show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity,Class aClass) {
        // initialize intent
        Intent intent = new Intent(activity, aClass);
        // set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start activity
        activity.startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        // close drawer
        closeDrawer(drawerLayout);
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
