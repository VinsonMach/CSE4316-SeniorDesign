package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class RingAuthData {
    @SerializedName("userId")
    int userId;

    @SerializedName("authCode")
    String authCode;

    @SerializedName("restClient")
    String restClient;

    public RingAuthData(int userId, String authCode, String restClient) {
        this.userId = userId;
        this.authCode = authCode;
        this.restClient = restClient;
    }
}
