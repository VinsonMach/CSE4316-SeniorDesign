package vib.track.cerberus.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "http://10.183.177.52:3000";
    private static Retrofit retrofit = null;

    private RetrofitClient() {
    }

    public static Retrofit getClient() {
        final OkHttpClient okClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okClient)
                    .build();
        }

        return retrofit;
    }
}
