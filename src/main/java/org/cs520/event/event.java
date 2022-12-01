package org.cs520.event;

import java.util.Dictionary;
import java.util.Hashtable;

abstract public class event {
    protected int ts; //global timestamp for this event inside the simulation
    protected int eventTime; //time needed for current event to finish
    protected String EventType;
    protected Dictionary<String,String> EventDetails = new Hashtable();

    public event (int t, String Type) {
        this.ts = t;
        this.EventType = Type;
    }

    abstract public void addDetail(String Key, String Value) ;

    abstract public String getDetail(String Key ) ;
    abstract public int getTS() ;
    abstract public void setTS(int newTS);

    abstract public int getEventTime() ;

    abstract public String getEventType();
}
