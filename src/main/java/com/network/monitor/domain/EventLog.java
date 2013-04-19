package com.network.monitor.domain;

import java.sql.Timestamp;

/**
 *
 * @author
 */
public class EventLog {

    Timestamp eventTime;
    
    String serverName;
    
    String message;
    
    EventType eventType;
    
    public Timestamp getEventTime() {
        return eventTime;
    }

    public void setEventTime(Timestamp eventTime) {
        this.eventTime = eventTime;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

   
}
