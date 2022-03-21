package vib.track.cerberus.data;

import java.sql.Date;
import java.sql.Time;

public class History {
    private String EventID, EventType, SensorID;
    private Date EventDate;
    private Time EventTime;
    int UserID;


    public String getEventID() {
        return EventID;
    }
    public void setEventID(String eventID) { this.EventID = eventID; }

    public String getEventType() {
        return EventType;
    }
    public void setEventType(String eventType) { this.EventType = eventType; }

    public Date getEventDate() {
        return EventDate;
    }
    public void setEventDate(Date eventDate) { this.EventDate = eventDate; }

    public Time getEventTime() {
        return EventTime;
    }
    public void setEventTime(Time eventTime) { this.EventTime = eventTime; }

    public int getUserID() {
        return UserID;
    }
    public void setUserID(int userID) { this.UserID = userID; }

    public String getSensorID() {
        return SensorID;
    }
    public void setSensorID(String sensorID) { this.SensorID = sensorID; }
}
