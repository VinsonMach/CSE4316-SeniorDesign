package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class NotifTokenData {
    @SerializedName("userId")
    int userId;

    @SerializedName("token")
    String token;

    public NotifTokenData(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
