package org.cs520.event;

import java.util.Dictionary;
import java.util.Hashtable;

abstract public class event {
    int ts; //global timestamp for this event inside the simulation
    int eventTime; //time needed for current event to finish
    String EventType;
    Dictionary<String,String> EventDetails = new Hashtable();

    public event (int t, String Type) {
        ts = t;
        EventType = Type;
    }

    public void addDetail(String Key, String Value) {
        EventDetails.put(Key, Value);
    }

    public String getDetail(String Key ) {
        return EventDetails.get(Key);
    }
    public int getTS(){
        return ts;
    }
    public void setTS(int newTS){ts = newTS;}

    public int getEventTime() {return eventTime;};

    public String getEventType(){
        return EventType;
    }
}
