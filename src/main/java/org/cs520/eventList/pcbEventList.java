package org.cs520.eventList;

import org.cs520.event.event;
import org.cs520.event.pcbEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class pcbEventList extends eventList{

    LinkedList<pcbEvent> EventDLL = new LinkedList<pcbEvent>();

    public pcbEventList() {
        super();
    }

    public int getEventSize() {
        return EventDLL.size();
    }

    public LinkedList<pcbEvent> getAllEvents(){
        //for debuging
        return EventDLL;
    };

    public pcbEvent peekLastEvent(){
        return EventDLL.peekLast();
    }
    public pcbEvent peekCurrentEvent(String ScheduleAlgo) {
        /*
         * We can implement the SJF and RR algo here
         * By default, poll() is same as FCFS
         * */
        if(ScheduleAlgo == "SJF"){
            Collections.sort(EventDLL, new Comparator<pcbEvent>() {
                        @Override
                        public int compare(pcbEvent s1, pcbEvent s2) {
                            return s1.getEventTime() - s2.getEventTime();
                        }
                    }
            );
            return EventDLL.peek();
        }else{
            return EventDLL.peek();
        }
    }

    public pcbEvent processCurrentEvent(String ScheduleAlgo) {
        /*
        * We can implement the SJF and RR algo here
        * By default, poll() is same as FCFS
        * */
        if(ScheduleAlgo == "SJF"){
            Collections.sort(EventDLL, new Comparator<pcbEvent>() {
                        @Override
                        public int compare(pcbEvent s1, pcbEvent s2) {
                            return s1.getEventTime() - s2.getEventTime();
                        }
                    }
            );
            return EventDLL.poll();
        }else{
            return EventDLL.poll();
        }

    }

    public void freezeEventList(int freezeT){
        //this is a customized function for pcb Event as CPU/IO device can only process 1 event(process) at a time
        //after each event.execute, simulation need to freeze all subsequent event by the CPU/IO time
        for (pcbEvent tmpEvent : EventDLL) {
            int currentTS = tmpEvent.getTS();
            tmpEvent.setTS(currentTS + freezeT);
        }
    }

    public void addEvent(pcbEvent newEvent, int EventTime, int ts){
        if(EventTime < 0){
            // if eventTime is invalid, use execute to generate a new one
            // this is for adding to CPU and IO device
            newEvent.execute(ts+1); //use execute to generate EventTime, execute also update EventType if needed.
            EventDLL.add(newEvent);
        }else{
            newEvent.setTS(ts+1);
            newEvent.setEventTime(EventTime);
            EventDLL.add(newEvent);
        }
    }

}
