package org.cs520.simulation;

import org.common.nextevent.*;
import org.cs520.event.*;
import org.cs520.eventList.*;
import org.cs520.pcb.*;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        System.out.println("Start Simulation");
        //NextEventTimeE eventTimeGenerator = new NextEventTimeE(0.03333);
        //double nextTime = eventTimeGenerator.getNextEventTime();
        //System.out.println(nextTime); //39.491732258129765
        NextEventTimeU eventTimeGeneratorU = new NextEventTimeU(2,4, 23);
        double nextUTime = eventTimeGeneratorU.getNextEventTime();
        System.out.println(nextUTime); //3.4649813252059607
        pcb sampleProcess = new pcb("0x00", (int) nextUTime*60000 ,0.03333 , 19); //0.0333 lambda --> avg 30
        System.out.println("testing pcb methods");
        System.out.println(sampleProcess.getStatus());
        System.out.println(sampleProcess.getMeanIoInterval());
        System.out.println(sampleProcess.getNextIOInterval());
        System.out.println(sampleProcess.getExecutionTime());
        System.out.println("testing event methods");
        pcbEvent sampleEvent = new pcbEvent(0,"CPU",sampleProcess);
        pcbEvent nextSampleEvent = sampleEvent.execute();
        System.out.println(nextSampleEvent.getTS());
        System.out.println(nextSampleEvent.getEventType());
        sampleProcess = nextSampleEvent.getProcess();
        System.out.println("next event pcb:");
        System.out.println(sampleProcess.getStatus());
        System.out.println(sampleProcess.getMeanIoInterval());
        System.out.println(sampleProcess.getNextIOInterval());
        System.out.println(sampleProcess.getExecutionTime());
        System.out.println("texting eventList");
        sampleProcess = new pcb("0x00", (int) nextUTime*60000 ,0.03333 , 19); //0.0333 lambda --> avg 30
        sampleEvent = new pcbEvent(0,"CPU",sampleProcess);
        eventList sampleList = new eventList(sampleEvent);
        pcbEvent currentEvent = (pcbEvent) sampleList.processCurrentEvent();
        nextSampleEvent = currentEvent.execute();
        sampleList.addEvent(nextSampleEvent);
        currentEvent = (pcbEvent) sampleList.processCurrentEvent();
        System.out.println(currentEvent.getTS());
        System.out.println(currentEvent.getEventType());
        sampleProcess = currentEvent.getProcess();
        System.out.println("last event pcb in eventList:");
        System.out.println(sampleProcess.getStatus());
        System.out.println(sampleProcess.getMeanIoInterval());
        System.out.println(sampleProcess.getNextIOInterval());
        System.out.println(sampleProcess.getExecutionTime());

        System.out.println("TESTING one process until process COMPLETE");
        Queue<pcb> processQueue = new LinkedList<>();
        sampleProcess = new pcb("0x00", (int) nextUTime*60000 ,0.03333 , 19); //0.0333 lambda --> avg 30
        processQueue.add(sampleProcess);
        sampleEvent = new pcbEvent(0,"CPU",sampleProcess);
        sampleList = new eventList(sampleEvent);
        while(processQueue.size() > 0){
            currentEvent = (pcbEvent) sampleList.processCurrentEvent();
            System.out.println(currentEvent.getTS());
            System.out.println(currentEvent.getEventType());
            sampleProcess = currentEvent.getProcess();
            System.out.println(sampleProcess.getStatus());
            System.out.println(sampleProcess.getExecutionTime());
            if(sampleProcess.getStatus() == "Complete"){
                processQueue.removeIf(e -> (e.getStatus() == "Complete"));
            }
            nextSampleEvent = currentEvent.execute();
            sampleList.addEvent(nextSampleEvent);
        }

    }
}