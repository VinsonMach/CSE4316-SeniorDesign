package vib.track.cerberus.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient_historypage {
    private final static String BASE_URL = "http://161.35.224.42:3000";
    private static Retrofit retrofit = null;
    private static RetrofitClient_historypage instance = null;
    private HistoryPageAPI myApi;

    private RetrofitClient_historypage() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(HistoryPageAPI.class);
    }

    public static synchronized RetrofitClient_historypage getInstance() {
        if (instance == null) {
            instance = new RetrofitClient_historypage();
        }
        return instance;
    }

    public HistoryPageAPI getMyApi() {
        return myApi;
    }
}