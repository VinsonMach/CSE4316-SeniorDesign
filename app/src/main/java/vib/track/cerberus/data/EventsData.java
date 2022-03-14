package vib.track.cerberus.data;

public class EventsData {
    private String EventID, EventType, EventDate, EventTime, UserID, SensorID;


    public String getEventID() {
        return EventID;
    }
    public void setEventID(String EventID) { this.EventID = EventID; }

    public String getEventType() {
        return EventType;
    }
    public void setEventType(String EventType) { this.EventType = EventType; }

    public String getEventDate() {
        return EventDate;
    }
    public void setEventDate(String EventDate) { this.EventDate = EventDate; }

    public String getEventTime() {
        return EventTime;
    }
    public void setEventTime(String EventTime) { this.EventTime = EventTime; }

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String UserID) { this.UserID = UserID; }

    public String getSensorID() {
        return SensorID;
    }
    public void setSensorID(String SensorID) { this.SensorID = SensorID; }
}
