package vib.track.cerberus.data.model;

import com.google.gson.annotations.SerializedName;

public class HistoryPageEvent {
    @SerializedName("eventName")
    String eventName;

    @SerializedName("eventDate")
    String eventDate;

    @SerializedName("eventTime")
    String eventTime;

    @SerializedName("eventType")
    String eventType;

    public HistoryPageEvent(String eventName,
                            String eventDate,
                            String eventTime,
                            String eventType) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventType = eventType;
    }

    public String getName() {
        return eventName;
    }

}
