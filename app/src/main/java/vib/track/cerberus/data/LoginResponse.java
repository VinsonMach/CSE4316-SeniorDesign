package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private int userId;

    @SerializedName("ringLogin")
    private boolean ringLogin;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isRefreshToken() { return ringLogin; }
}
