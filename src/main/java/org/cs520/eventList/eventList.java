package org.cs520.eventList;

import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Dictionary;
import java.util.Collections;
import java.util.Comparator;
import org.cs520.event.event;
import org.cs520.event.pcbEvent;

public class eventList {
    LinkedList<event> EventDLL = new LinkedList<event>();

    public eventList(event firstEvent){
        EventDLL.add(0, firstEvent);
    }

    public int getEventSize() {
        return EventDLL.size();
    }

    public void addEvent(event newEvent) {
        //add event based on ts
        int ts = newEvent.getTS();
        //scan the EventDLL and insert the event to 'right position'
        EventDLL.add(newEvent);//EventDLL.add(ts, event);
        //Whenever we add new future event, we need to sort remaining list
        Collections.sort(EventDLL, new Comparator<event>() {
                    @Override
                    public int compare(event s1, event s2) {
                        return s1.getTS() - s2.getTS();
                    }
                }
        );
        System.out.println("--->Event Added and reordered");
    }

    public event getEvent(int ts) {
        return EventDLL.get(ts);
    }

    public event processCurrentEvent() {
        return EventDLL.poll();
    }

}
