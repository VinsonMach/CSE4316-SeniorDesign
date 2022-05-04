package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class HistoryCallData {
    @SerializedName("userId")
    int userId;

    public HistoryCallData(int userId) {
        this.userId = userId;
    }
}
