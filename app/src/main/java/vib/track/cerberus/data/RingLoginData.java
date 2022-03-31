package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class RingLoginData {
    @SerializedName("userEmail")
    String userEmail;

    @SerializedName("userPassword")
    String userPassword;

    public RingLoginData(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
