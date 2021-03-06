package vib.track.cerberus.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vib.track.cerberus.data.HistoryCallData;
import vib.track.cerberus.data.JoinData;
import vib.track.cerberus.data.JoinResponse;
import vib.track.cerberus.data.LoginData;
import vib.track.cerberus.data.LoginResponse;
import vib.track.cerberus.data.NotifTokenData;
import vib.track.cerberus.data.NotifTokenResponse;
import vib.track.cerberus.data.RingAuthData;
import vib.track.cerberus.data.RingAuthResponse;
import vib.track.cerberus.data.RingLoginData;
import vib.track.cerberus.data.RingLoginResponse;
import vib.track.cerberus.data.RingNotifyData;
import vib.track.cerberus.data.RingNotifyResponse;
import vib.track.cerberus.data.SingleEvent;

public interface ServiceApi {
    @POST("/user/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/user/join")
    Call<JoinResponse> userJoin(@Body JoinData data);

    @POST("/ring/login")
    Call<RingLoginResponse> ringLogin(@Body RingLoginData data);

    @POST("/ring/auth")
    Call<RingAuthResponse> ringAuth(@Body RingAuthData data);

    @POST("/ring/notify")
    Call<RingNotifyResponse> ringNotify(@Body RingNotifyData data);

    @POST("/ring/event")
    Call<SingleEvent> getData(@Body HistoryCallData data);

    @POST("/notif/token")
    Call<NotifTokenResponse> notifToken(@Body NotifTokenData data);
}
