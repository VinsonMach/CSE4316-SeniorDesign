package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class RingNotifyData {
    @SerializedName("userId")
    int userID;

    public RingNotifyData(int userID) {
        this.userID = userID;
    }
}
