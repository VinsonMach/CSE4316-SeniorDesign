package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class RingAuthData {
    @SerializedName("userId")
    int userId;

    @SerializedName("authCode")
    String authCode;

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    public RingAuthData(int userId, String authCode, String email, String password) {
        this.userId = userId;
        this.authCode = authCode;
        this.email = email;
        this.password = password;
    }
}
