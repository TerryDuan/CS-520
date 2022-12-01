package org.cs520.eventList;

import org.cs520.event.pcbEvent;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class pcbEventList extends eventList{

    LinkedList<pcbEvent> EventDLL = new LinkedList<pcbEvent>();

    public pcbEventList() {
        super();
    }

    public pcbEvent peekLastEvent(){
        return EventDLL.peekLast();
    }

    public void freezeEventList(int freezeT){
        //this is a customized function for pcb Event as CPU/IO device can only process 1 event(process) at a time
        //after each event.execute, simulation need to freeze all subsequent event by the CPU/IO time
        for (pcbEvent tmpEvent : EventDLL) {
            int currentTS = tmpEvent.getTS();
            tmpEvent.setTS(currentTS + freezeT);
        }
    }

    public void addEvent(pcbEvent newEvent, boolean FIFO){
        if(FIFO){
            //add and sort the list based on TS
            //add event based on ts
            //scan the EventDLL and insert the event to 'right position'
            EventDLL.add(newEvent);//EventDLL.add(ts, event);
            //Whenever we add new future event, we need to sort remaining list
            Collections.sort(EventDLL, new Comparator<pcbEvent>() {
                        @Override
                        public int compare(pcbEvent s1, pcbEvent s2) {
                            return s1.getTS() - s2.getTS();
                        }
                    }
            );
            System.out.println("--->Event Added and reordered based on TS arrival time");
        }else{
            //SJF, add and sort by pcb's remaining execution time
            EventDLL.add(newEvent);
            Collections.sort(EventDLL, new Comparator<pcbEvent>() {
                        @Override
                        public int compare(pcbEvent s1, pcbEvent s2) {
                            return s1.getProcess().getExecutionTime() - s2.getProcess().getExecutionTime();
                        }
                    }
            );
        }
    }

}
