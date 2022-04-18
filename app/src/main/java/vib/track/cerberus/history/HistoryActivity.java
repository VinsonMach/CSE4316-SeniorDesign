package vib.track.cerberus.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.data.HistoryData;
import vib.track.cerberus.data.SingleEvent;
import vib.track.cerberus.network.RetrofitClient;
import vib.track.cerberus.network.ServiceApi;

public class HistoryActivity extends AppCompatActivity {

    SingleEvent dataList;
    List<HistoryData> historyDataInfo;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyDataInfo = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

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
}