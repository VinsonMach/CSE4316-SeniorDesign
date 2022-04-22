package vib.track.cerberus.history;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.data.HistoryData;
import vib.track.cerberus.data.SingleEvent;
import vib.track.cerberus.home.HomepageActivity;
import vib.track.cerberus.home.SecondFragment;
import vib.track.cerberus.home.credits;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;

public class HistoryActivity extends AppCompatActivity {

    SingleEvent dataList;
    List<HistoryData> historyDataInfo;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyDataInfo = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawer_layout);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), historyDataInfo);
        recyclerView.setAdapter(recyclerAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ServiceApi serviceApi = RetrofitClient.getClient().create(ServiceApi.class);

        Call<SingleEvent> call = serviceApi.getData();

        call.enqueue(new Callback<SingleEvent>() {
            @Override
            public void onResponse(Call<SingleEvent> call, Response<SingleEvent> response) {

                dataList = response.body();

                Log.d("MainActivity", dataList.toString());

                historyDataInfo = dataList.body;

                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), historyDataInfo);
                recyclerView.setAdapter(recyclerAdapter);

            }

            @Override
            public void onFailure(Call<SingleEvent> call, Throwable t) {

                Log.d("MainActivity", t.toString());
            }
        });
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // close drawer when drawer is open
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void clickHome(View view){
        // redirect to history
        HomepageActivity.redirectActivity(this, HomepageActivity.class);
    }

    public void clickHistory(View view){
        // recreate history
        recreate();
    }

    public void clickSettings(View view){
        // redirect settings activity
        HomepageActivity.redirectActivity(this, SecondFragment.class);
    }

    public void clickCredits(View view){
        // redirect to credits
        HomepageActivity.redirectActivity(this, credits.class);
    }

    public void clickLogout(View view){
        // redirect to history
        HomepageActivity.logout(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        // close drawer
        HomepageActivity.closeDrawer(drawerLayout);
    }
}
