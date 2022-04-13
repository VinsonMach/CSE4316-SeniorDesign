package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    private String code;

    @SerializedName("ringLogin")
    private boolean ringLogin;

    @SerializedName("userId")
    private String userId;

    @SerializedName("hashedPwd")
    private String hashedpassword;

    public String getCode() {
        return code;
    }

    public boolean getringLogin() { return ringLogin; }

    public String getUserId() { return userId; }

    public String getHashedpassword() { return hashedpassword; }
}
