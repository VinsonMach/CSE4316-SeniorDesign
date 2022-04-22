package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleEvent {
    @SerializedName("statusCode")
    public int statusCode;
    @SerializedName("message")
    public String message;
    @SerializedName("body")
    public List<HistoryData> body;

    @Override
    public String toString() {
        return "TestItem{" + "body=" + body + "}";
    }
}
