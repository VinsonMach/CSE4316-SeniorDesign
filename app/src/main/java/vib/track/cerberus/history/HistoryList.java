package vib.track.cerberus.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vib.track.cerberus.R;
import vib.track.cerberus.data.History;
import vib.track.cerberus.network.HistoryPageAPI;
import vib.track.cerberus.network.RetrofitClient_historypage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryList extends Fragment {
    private HistoryPageAPI service;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryList.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryList newInstance(String param1, String param2) {
        HistoryList fragment = new HistoryList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        return inflater.inflate(R.layout.fragment_history_list, container, false);
    }
}