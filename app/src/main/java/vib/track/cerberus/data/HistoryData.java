package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class HistoryData {
    @SerializedName("EventID")
    private int EventID;
    @SerializedName("SensorID")
    private String SensorID;
    @SerializedName("SensorName")
    private String SensorName;
    @SerializedName("EventType")
    private String EventType;
    @SerializedName("EventDate")
    private String EventDate;
    @SerializedName("EventTime")
    private String EventTime;
    @SerializedName("UserID")
    private int UserID;

    @Override
    public String toString() {
        return "Data{" +
                "EventID=" + EventID +
                ", SensorID='" + SensorID + '\'' +
                ", SensorName='" + SensorName + '\'' +
                ", EventType='" + EventType + '\'' +
                ", EventDate='" + EventDate + '\'' +
                ", EventTime='" + EventTime + '\'' +
                ", UserID=" + UserID +
                '}';
    }

    public int getEventID() {
        return EventID;
    }

    public String getSensorID() {
        return SensorID;
    }

    public String getSensorName() {
        return SensorName;
    }

    public String getEventType() {
        return EventType;
    }

    public String getEventDate() {
        return EventDate;
    }

    public String getEventTime() {
        return EventTime;
    }

    public int getUserID() {
        return UserID;
    }
}