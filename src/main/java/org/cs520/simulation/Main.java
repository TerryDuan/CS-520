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
        /*
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
        */

        /*
        * For this simulation, we only has 1 CPU and 1 I/O device
        * Create two (pcb)EventList for CPU readyQueue and I/O readyQueue
        * EventList has the feature to gurantee the event(item) is sorted based on simulation TS (in millisecound)
        * While loop until both EventLists are empty (If a pcb status is complete, add no event to any queue --> gurantee stop)
        *   in each iteration, compare the head of both queue's TS, and execute the latest most recent one.
        *   pcbEvent.execute() return next event
        * */

        System.out.println("TESTING 2 process until process COMPLETE");
        double nextUTime;
        //Queue<pcb> processQueue = new LinkedList<>();
        pcbEventList readyQueue = new pcbEventList();
        pcbEventList ioQueue = new pcbEventList();
        NextEventTimeU eventTimeGeneratorU = new NextEventTimeU(2,4, 23);

        //adding process
        nextUTime = eventTimeGeneratorU.getNextEventTime();
        pcb sampleProcess1 = new pcb("0x01", (int) nextUTime*60000 ,0.03333 , 19); //0.0333 lambda --> avg 30
        nextUTime = eventTimeGeneratorU.getNextEventTime();
        pcb sampleProcess2 = new pcb("0x02", (int) nextUTime*60000 ,0.025 , 23); //0.025 lambda --> avg 40
        nextUTime = eventTimeGeneratorU.getNextEventTime();
        pcb sampleProcess3 = new pcb("0x03", (int) nextUTime*60000 ,0.02857 , 47); //0.02857 lambda --> avg 35
        pcbEvent process1event = new pcbEvent(0,"CPU", sampleProcess1);
        pcbEvent process2event = new pcbEvent(1, "CPU", sampleProcess2);
        pcbEvent process3event = new pcbEvent(2, "CPU", sampleProcess3);
        readyQueue.addEvent(process1event,true);
        readyQueue.addEvent(process2event,true);
        readyQueue.addEvent(process3event,true);
        System.out.println("Initiated Queue, size: "+ readyQueue.getEventSize());

        int clock = 0;
        pcbEvent currentEvent;
        pcbEvent currentEventIO;
        pcbEvent nextEvent;
        pcbEvent tailEvent;
        int CPUtime;
        String nextEventType;

        while(clock < 500){

            if (readyQueue.getEventSize() > 0){
                currentEvent = (pcbEvent) readyQueue.peekCurrentEvent();
            }

            if (ioQueue.getEventSize() > 0){
                currentEventIO = (pcbEvent) ioQueue.peekCurrentEvent();
            }

            if (readyQueue.getEventSize() > 0){
                currentEvent = (pcbEvent) readyQueue.peekCurrentEvent();
                    if(clock == currentEvent.getTS()){
                        System.out.println("TS: " + clock);
                        System.out.println("----time to process current event at head of queue");
                        currentEvent = (pcbEvent) readyQueue.processCurrentEvent();
                        System.out.println("----current event " + currentEvent.getEventType());
                        System.out.println("----current event process " + currentEvent.getProcess().getProcessID());
                        nextEvent = currentEvent.execute();
                        CPUtime = currentEvent.getEventTime();
                        System.out.println("----current event finish TS (nextEvent TS) " + nextEvent.getTS());
                        System.out.println("----next event " + nextEvent.getEventType());
                        System.out.println("----current(to be finished) event eventTime " + CPUtime);

                        //updating all queue based on newEvent type
                        nextEventType = nextEvent.getEventType();

                        System.out.println("----freeze all remaining event in current queue for " + CPUtime);
                        if (nextEventType == "IO"){
                            readyQueue.freezeEventList(CPUtime);
                            System.out.println("----addjusting TS for nextEvent based on tail event before eventList.add() , new TS = tail TS + 1"); // +1 to avoid arbitrary ordering
                            if(ioQueue.getEventSize() > 0){
                                tailEvent = ioQueue.peekLastEvent();
                                nextEvent.setTS(tailEvent.getTS() + 1);
                            }else{
                                nextEvent.setTS(nextEvent.getTS() + 1);
                            }
                            ioQueue.addEvent(nextEvent,true);
                        }else{
                            ioQueue.freezeEventList(CPUtime);
                            System.out.println("----addjusting TS for nextEvent based on tail event before eventList.add() , new TS = tail TS + 1"); // +1 to avoid arbitrary ordering
                            if(readyQueue.getEventSize() > 0){
                                tailEvent = readyQueue.peekLastEvent();
                                nextEvent.setTS(tailEvent.getTS() + 1);
                            }else{
                                nextEvent.setTS(nextEvent.getTS() + 1);
                            }

                            if (nextEvent.getProcess().getStatus() != "Complete"){
                                readyQueue.addEvent(nextEvent,true);
                            }else{
                                System.out.println("------process COMPLETED, do not adding back to any queue");
                                continue;
                            }
                        }
                        //check queue
                        System.out.println("Current CPU Queue Size " + readyQueue.getEventSize());
                        System.out.println("Current I/O Queue Size " + ioQueue.getEventSize());
                        System.out.print("CPU Queue: |-> ");
                        for (pcbEvent tmpEvent : readyQueue.getAllEvents()) {
                            String processID = tmpEvent.getProcess().getProcessID();
                            System.out.print(processID + "," + tmpEvent.getTS() + " -> ");
                        }
                        System.out.println(" | TAIL ");

                        System.out.print("I/O Queue: |-> ");
                        for (pcbEvent tmpEvent : ioQueue.getAllEvents()) {
                            String processID = tmpEvent.getProcess().getProcessID();
                            System.out.print(processID + "," + tmpEvent.getTS() + " -> ");
                        }
                        System.out.println(" | TAIL ");

                        System.out.println(" *************************** ");
                }
            }
            if (ioQueue.getEventSize() > 0){
                currentEvent = (pcbEvent) ioQueue.peekCurrentEvent();
                if (clock == currentEvent.getTS()){
                    System.out.println("TS: " + clock);
                    System.out.println("----time to process current event at head of queue");
                    currentEvent = (pcbEvent) ioQueue.processCurrentEvent();
                    System.out.println("----current event " + currentEvent.getEventType());
                    System.out.println("----current event process " + currentEvent.getProcess().getProcessID());
                    nextEvent = currentEvent.execute();
                    CPUtime = currentEvent.getEventTime();
                    System.out.println("----current event finish TS (nextEvent TS) " + nextEvent.getTS());
                    System.out.println("----next event " + nextEvent.getEventType());
                    System.out.println("----current(to be finished) event eventTime " + CPUtime);

                    //updating all queue based on newEvent type
                    nextEventType = nextEvent.getEventType();

                    System.out.println("----freeze all remaining event in current queue for " + CPUtime);
                    if (nextEventType == "IO"){
                        readyQueue.freezeEventList(CPUtime);
                        System.out.println("----addjusting TS for nextEvent based on tail event before eventList.add() , new TS = tail TS + 1"); // +1 to avoid arbitrary ordering
                        if(ioQueue.getEventSize() > 0){
                            tailEvent = ioQueue.peekLastEvent();
                            nextEvent.setTS(tailEvent.getTS() + 1);
                        }else{
                            nextEvent.setTS(nextEvent.getTS() + 1);
                        }
                        ioQueue.addEvent(nextEvent,true);
                    }else{
                        ioQueue.freezeEventList(CPUtime);
                        System.out.println("----addjusting TS for nextEvent based on tail event before eventList.add() , new TS = tail TS + 1"); // +1 to avoid arbitrary ordering
                        if(readyQueue.getEventSize() > 0){
                            tailEvent = readyQueue.peekLastEvent();
                            nextEvent.setTS(tailEvent.getTS() + 1);
                        }else{
                            nextEvent.setTS(nextEvent.getTS() + 1);
                        }

                        if (nextEvent.getProcess().getStatus() != "Complete"){
                            readyQueue.addEvent(nextEvent,true);
                        }else{
                            System.out.println("------process COMPLETED, do not adding back to any queue");
                            continue;
                        }
                    }
                    System.out.println("Current CPU Queue Size " + readyQueue.getEventSize());
                    System.out.println("Current I/O Queue Size " + ioQueue.getEventSize());
                    System.out.print("CPU Queue: |-> ");
                    for (pcbEvent tmpEvent : readyQueue.getAllEvents()) {
                        String processID = tmpEvent.getProcess().getProcessID();
                        System.out.print(processID + "," + tmpEvent.getTS() + " -> ");
                    }
                    System.out.println(" | TAIL ");

                    System.out.print("I/O Queue: |-> ");
                    for (pcbEvent tmpEvent : ioQueue.getAllEvents()) {
                        String processID = tmpEvent.getProcess().getProcessID();
                        System.out.print(processID + "," + tmpEvent.getTS() + " -> ");
                    }
                    System.out.println(" | TAIL ");

                    System.out.println(" *************************** ");
                }
            }



            //early stop:
            if((readyQueue.getEventSize() == 0) & (ioQueue.getEventSize() == 0)){
                break;
            }
            clock = clock + 1;
        }




    }
}