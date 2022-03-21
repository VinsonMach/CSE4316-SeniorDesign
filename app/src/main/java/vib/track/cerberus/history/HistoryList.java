package vib.track.cerberus.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.data.History;
import vib.track.cerberus.network.HistoryPageAPI;
import vib.track.cerberus.network.RetrofitClient_historypage;


public class HistoryList extends Fragment {

    private HistoryPageAPI service;
    private List<History> history;

    public HistoryList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "inside oncreate", Toast.LENGTH_LONG).show();

        Call<List<History>> call = RetrofitClient_historypage.getInstance().getMyApi().showEvents();
        call.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {

            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {

            }
        });

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_history_list, container, false);
        history = (List<History>) service.showEvents();

        RecyclerView recyclerView = v.findViewById(R.id.HistoryRecycler);

        RecyclerView.LayoutManager vLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(vLayoutManager);

        HistoryAdapter adapter = new HistoryAdapter(history);
        recyclerView.setAdapter(adapter);

        return v;
    }
}