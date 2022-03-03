package vib.track.cerberus.network;

import vib.track.cerberus.data.JoinData;
import vib.track.cerberus.data.JoinResponse;
import vib.track.cerberus.data.LoginData;
import vib.track.cerberus.data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);
}