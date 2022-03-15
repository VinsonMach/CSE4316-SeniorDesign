package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class RingAuthResponse {
    @SerializedName("resultCode")
    private int resultCode;

    @SerializedName("message")
    private String message;

    public int getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }
}
