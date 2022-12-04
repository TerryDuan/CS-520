package org.cs520.simulation;

import org.common.nextevent.*;
import org.cs520.event.*;
import org.cs520.eventList.*;
import org.cs520.pcb.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        String fileName = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());
        int CPU_idleTime = 0;
        int CPU_executedProcess = 0;
        String ScheduleAlgorithm = "SJF"; //"FCFS" "SJF"
        int Quantum = 30; //1000000
        List<Integer> GanttChart = new ArrayList<Integer>();
        int CPU_runTime = 0;
        PrintWriter writerAllrun = new PrintWriter("D:\\Files\\CS\\CS520_Project\\log\\Simulation_v3_" + ScheduleAlgorithm + "_RR" + "_30_1000" + "_" + fileName + ".log", "UTF-8");

        while (Quantum < 1000) {
            System.out.println("Quantum: " + Quantum);
            PrintWriter writer = new PrintWriter("D:\\Files\\CS\\CS520_Project\\log\\Simulation_v3_" + ScheduleAlgorithm + "_RR" + Quantum + "_" + fileName + ".log", "UTF-8");

            System.out.println("Start Simulation");

            /*
             * For this simulation, we only has 1 CPU and 1 I/O device
             * Create a EventList for CPUQueue, IOQueue, CPUdevice, IOdevice
             * Loop thru time and check if CPU and IO device is availible
             * */

            //System.out.println("TESTING 2 process until process COMPLETE");
            double nextUTime;
            //Queue<pcb> processQueue = new LinkedList<>();
            pcbEventList readyQueue = new pcbEventList();
            pcbEventList ioQueue = new pcbEventList();
            pcbEventList CPUdevice = new pcbEventList();
            pcbEventList IOdevice = new pcbEventList();
            NextEventTimeU eventTimeGeneratorU = new NextEventTimeU(2, 4, 23);

            //init and adding process
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess1 = new pcb("0x01", (int) nextUTime * 60000, 0.03333, 19); //0.0333 lambda --> avg 30
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess2 = new pcb("0x02", (int) nextUTime * 60000, 0.025, 23); //0.025 lambda --> avg 40
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess3 = new pcb("0x03", (int) nextUTime * 60000, 0.02857, 47); //0.02857 lambda --> avg 35
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess4 = new pcb("0x04", (int) nextUTime * 60000, 0.02222, 51); //0.02222 lambda --> avg 45
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess5 = new pcb("0x05", (int) nextUTime * 60000, 0.02, 53); //0.02 lambda --> avg 50
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess6 = new pcb("0x06", (int) nextUTime * 60000, 0.01818, 61); //0.01818 lambda --> avg 55
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess7 = new pcb("0x07", (int) nextUTime * 60000, 0.01667, 63); //0.01667 lambda --> avg 60
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess8 = new pcb("0x08", (int) nextUTime * 60000, 0.01538, 73); //0.01538 lambda --> avg 65
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess9 = new pcb("0x09", (int) nextUTime * 60000, 0.01429, 77); //0.01429 lambda --> avg 70
            nextUTime = eventTimeGeneratorU.getNextEventTime();
            pcb sampleProcess0 = new pcb("0x00", (int) nextUTime * 60000, 0.01333, 83); //0.01333 lambda --> avg 75

            pcbEvent process1event = new pcbEvent(0, "CPU", sampleProcess1);
            pcbEvent process2event = new pcbEvent(0, "CPU", sampleProcess2);
            pcbEvent process3event = new pcbEvent(0, "CPU", sampleProcess3);
            pcbEvent process4event = new pcbEvent(0, "CPU", sampleProcess4);
            pcbEvent process5event = new pcbEvent(0, "CPU", sampleProcess5);
            pcbEvent process6event = new pcbEvent(0, "CPU", sampleProcess6);
            pcbEvent process7event = new pcbEvent(0, "CPU", sampleProcess7);
            pcbEvent process8event = new pcbEvent(0, "CPU", sampleProcess8);
            pcbEvent process9event = new pcbEvent(0, "CPU", sampleProcess9);
            pcbEvent process0event = new pcbEvent(0, "CPU", sampleProcess0);

            readyQueue.addEvent(process1event, -1, 0);
            readyQueue.addEvent(process2event, -1, 0);
            readyQueue.addEvent(process3event, -1, 0);
            readyQueue.addEvent(process4event, -1, 0);
            readyQueue.addEvent(process5event, -1, 0);
            readyQueue.addEvent(process6event, -1, 0);
            readyQueue.addEvent(process7event, -1, 0);
            readyQueue.addEvent(process8event, -1, 0);
            readyQueue.addEvent(process9event, -1, 0);
            readyQueue.addEvent(process0event, -1, 0);

            Hashtable<String, Integer> processWaitTime = new Hashtable<String, Integer>();

            processWaitTime.put("0x01", 0);
            processWaitTime.put("0x02", 0);
            processWaitTime.put("0x03", 0);
            processWaitTime.put("0x04", 0);
            processWaitTime.put("0x05", 0);
            processWaitTime.put("0x06", 0);
            processWaitTime.put("0x07", 0);
            processWaitTime.put("0x08", 0);
            processWaitTime.put("0x09", 0);
            processWaitTime.put("0x00", 0);

            System.out.println("Initiated Queue, size: " + readyQueue.getEventSize());

            int clock = 0;
            pcbEvent currentEvent;
            pcbEvent currentEventIO;
            pcbEvent nextEvent;
            pcbEvent tailEvent;
            int CPUtime;
            int tempExct;
            String nextEventType;
            pcb tempProcess;

            while (true) {

                if (clock % 100000 == 0) {
                    //System.out.println(clock);
                /*
                writer.print(clock);
                if (CPUdevice.getEventSize() > 0){
                    writer.print(" CPU Device: |-> " + CPUdevice.peekCurrentEvent("FCFS").getProcess().getProcessID() + ", TS " + CPUdevice.peekCurrentEvent("FCFS").getTS() + ", ExecT " + CPUdevice.peekCurrentEvent("FCFS").getProcess().getExecutionTime());
                }
                else{
                    writer.print(" CPU Device: |->| ");
                }
                writer.print(" CPU Queue: |-> ");
                for (pcbEvent tmpEvent : readyQueue.getAllEvents()) {
                    String processID = tmpEvent.getProcess().getProcessID();
                    writer.print(processID + "," + tmpEvent.getTS() + "," + tmpEvent.getProcess().getExecutionTime() + "," + tmpEvent.getEventTime() + " -> ");
                }
                writer.println(" | TAIL ");
                writer.print(clock);
                if (IOdevice.getEventSize() > 0){
                    writer.print(" IO Device: |-> " + IOdevice.peekCurrentEvent("FCFS").getProcess().getProcessID() + ", TS " + IOdevice.peekCurrentEvent("FCFS").getTS() + ", ExecT " + IOdevice.peekCurrentEvent("FCFS").getProcess().getExecutionTime());
                }
                else{
                    writer.print(" IO Device: |->| ");
                }
                writer.print(" IO Queue: |-> ");
                for (pcbEvent tmpEvent : ioQueue.getAllEvents()) {
                    String processID = tmpEvent.getProcess().getProcessID();
                    writer.print(processID + "," + tmpEvent.getTS() + "," + tmpEvent.getProcess().getExecutionTime() + "," + tmpEvent.getEventTime() + " -> ");
                }
                writer.println(" | TAIL ");
                */
                }

                //System.out.println("CPU device: ");
                if ((readyQueue.getEventSize() > 0) & (CPUdevice.getEventSize() == 0)) {
                    // if CPU is empty(available) and there is process in queue
                    // Test differenct Scheduling ALGO here:::
                    currentEvent = (pcbEvent) readyQueue.processCurrentEvent(ScheduleAlgorithm);
                    //currentEvent.execute(clock); //this method should generate required event time using random generator
                    //currentEvent.setTS(currentEvent.getEventTime() + clock + 1);
                    currentEvent.setEventType("CPU");
                    CPUdevice.addEvent(currentEvent, currentEvent.getEventTime(), clock + currentEvent.getEventTime()); //use eventTime determined in queue
                } else if (CPUdevice.getEventSize() > 0) {
                    //check if current pcb in CPUdevice is finished now:
                    if (clock == CPUdevice.peekCurrentEvent("FCFS").getTS()) {
                        currentEvent = (pcbEvent) CPUdevice.processCurrentEvent("FCFS");
                        GanttChart.add(CPU_runTime);
                        CPU_runTime = 0;
                        CPU_executedProcess = CPU_executedProcess + 1;
                        if (currentEvent.getProcess().getStatus() != "Complete") {
                            currentEvent.setEventType("IO");
                            ioQueue.addEvent(currentEvent, -1, clock);
                        }
                        //}else if((CPUdevice.peekCurrentEvent("FCFS").getEventTime() - CPUdevice.peekCurrentEvent("FCFS").getTS() + clock) > Quantum){
                    } else if (CPU_runTime > Quantum) {
                        //THIS IS FOR Round Robin
                        //if the current process being running for too long, remove it from CPUdevice
                        currentEvent = (pcbEvent) CPUdevice.processCurrentEvent("FCFS");
                        //System.out.println("--->current process " + currentEvent.getProcess().getProcessID() + " Remaining execT " + currentEvent.getProcess().getExecutionTime() + " EventTime : " + currentEvent.getEventTime());
                        //overwrite the remaining execution time
                        tempProcess = currentEvent.getProcess();
                        tempExct = tempProcess.getExecutionTime() + CPU_runTime; //(currentEvent.getTS() - 1 - clock);
                        tempProcess.setExecutionTime(tempExct); //add just remaining execT
                        currentEvent.setProcess(tempProcess);
                        //System.out.println("--->moved back to cpu Queue, current process " + currentEvent.getProcess().getProcessID() + " Remaining execT " + currentEvent.getProcess().getExecutionTime());

                        //add it back to cpu Queue
                        //Since process is already executed for allowed time (Quantum), adjust eventTime and add back to queue
                        currentEvent.setEventType("CPU");
                        readyQueue.addEvent(currentEvent, (currentEvent.getEventTime() - Quantum), clock);
                        GanttChart.add(CPU_runTime);
                        CPU_runTime = 0;
                    } else {
                        //pcb in CPU is still running
                        CPU_runTime = CPU_runTime + 1;
                        //System.out.println("CPU: Running");
                    }
                } else {
                    //no pcb in CPU ready Queue now, move on for now
                    //System.out.println("CPU Queue Empty");
                    if ((readyQueue.getEventSize() == 0) & (CPUdevice.getEventSize() == 0)) {
                        CPU_idleTime = CPU_idleTime + 1; //CPU now in idle
                    }
                }

                //System.out.println("IO device: ");
                if ((ioQueue.getEventSize() > 0) & (IOdevice.getEventSize() == 0)) {
                    // if CPU is empty(available) and there is process in queue
                    currentEvent = (pcbEvent) ioQueue.processCurrentEvent("FCFS");
                    currentEvent.execute(clock); //this method should generate required event time using random generator
                    currentEvent.setTS(currentEvent.getEventTime() + clock + 1);
                    currentEvent.setEventType("IO");
                    IOdevice.addEvent(currentEvent, -1, clock);
                } else if (IOdevice.getEventSize() > 0) {
                    //check if current pcb in IO device is finished
                    if (clock == IOdevice.peekCurrentEvent("FCFS").getTS()) {
                        currentEvent = (pcbEvent) IOdevice.processCurrentEvent("FCFS");
                        if (currentEvent.getProcess().getStatus() != "Complete") {
                            currentEvent.setEventType("CPU");
                            readyQueue.addEvent(currentEvent, -1, clock);
                        }
                    } else {
                        //pcb in IO is still running
                        //System.out.println("IO: Running");
                    }
                } else {
                    //no pcb in CPU ready Queue now, move on for now
                    //System.out.println("IO Queue Empty");
                }

                for (pcbEvent tmpEvent : readyQueue.getAllEvents()) {
                    String processID = tmpEvent.getProcess().getProcessID();
                    processWaitTime.put(processID, processWaitTime.get(processID) + 1);
                }


                //PRINT FOR DEBUG
            /*
            //System.out.println("*******************************************************************************************************");
            //System.out.println(clock);
            //System.out.println("readyQueue " + readyQueue.getEventSize());
            //System.out.println("CPUdevice " + CPUdevice.getEventSize());
            //System.out.println("ioQueue " + ioQueue.getEventSize());
            //System.out.println("IOdevice " + IOdevice.getEventSize());
            writer.print(clock);
            if (CPUdevice.getEventSize() > 0){
                writer.print(" CPU Device: |-> " + CPUdevice.peekCurrentEvent("FCFS").getProcess().getProcessID() + ", TS " + CPUdevice.peekCurrentEvent("FCFS").getTS() + ", ExecT " + CPUdevice.peekCurrentEvent("FCFS").getProcess().getExecutionTime());
            }
            else{
                writer.print(" CPU Device: |->| ");
            }
            writer.print(" CPU Queue: |-> ");
            for (pcbEvent tmpEvent : readyQueue.getAllEvents()) {
                String processID = tmpEvent.getProcess().getProcessID();
                writer.print(processID + "," + tmpEvent.getTS() + "," + tmpEvent.getProcess().getExecutionTime() + "," + tmpEvent.getEventTime() + " -> ");
            }
            writer.println(" | TAIL ");
            */
            /*
            System.out.print(clock);
            if (IOdevice.getEventSize() > 0){
                System.out.print(" IO Device: |-> " + IOdevice.peekCurrentEvent("FCFS").getProcess().getProcessID() + ", TS " + IOdevice.peekCurrentEvent("FCFS").getTS() + ", ExecT " + IOdevice.peekCurrentEvent("FCFS").getProcess().getExecutionTime());
            }
            else{
                System.out.print(" IO Device: |->| ");
            }
            System.out.print(" IO Queue: |-> ");
            for (pcbEvent tmpEvent : ioQueue.getAllEvents()) {
                String processID = tmpEvent.getProcess().getProcessID();
                System.out.print(processID + "," + tmpEvent.getTS() + "," + tmpEvent.getProcess().getExecutionTime() + "," + tmpEvent.getEventTime() + " -> ");
            }
            System.out.println(" | TAIL ");
            */

                //early stop:
                if ((readyQueue.getEventSize() == 0) & (ioQueue.getEventSize() == 0) & (CPUdevice.getEventSize() == 0) & (IOdevice.getEventSize() == 0)) {
                    break;
                }
                clock = clock + 1;

                //if(clock > 100000000){
                //    break;
                //}
            }

            /*
            System.out.println("Total Clock Time " + clock);
            System.out.println("Total CPU Idle Time " + CPU_idleTime);
            System.out.println("CPU ThroughPut (per second) " + (double) CPU_executedProcess / (double) clock / 1000.0);
            System.out.println("processID|Total Waiting for CPU");
             */
            double totalWait = 0.0;
            for (String pid : processWaitTime.keySet()) {
                //System.out.println(pid + "|" + processWaitTime.get(pid));
                totalWait = totalWait + (double) processWaitTime.get(pid);
            }
            System.out.println("Average Wait time: " + totalWait / 10.0);
            writerAllrun.println(Quantum+"|"+totalWait / 10.0);

            writer.println("Scheduling Algo: " + ScheduleAlgorithm);
            writer.println("Quantum for RR " + Quantum);
            writer.println("Total Clock Time " + clock);
            writer.println("Total CPU Idle Time " + CPU_idleTime);
            double utilization = 1.0 - ((double) CPU_idleTime / (double) clock);
            writer.println("Total CPU Untilization Rate " + utilization);
            writer.println("CPU ThroughPut (per second) " + (double) CPU_executedProcess / (double) clock / 1000.0);
            writer.println("-------------------------------");
            writer.println("processID|Total Waiting for CPU");
            totalWait = 0.0;
            for (String pid : processWaitTime.keySet()) {
                writer.println(pid + "|" + processWaitTime.get(pid));
                totalWait = totalWait + (double) processWaitTime.get(pid);
            }
            writer.println("Average Wait time: " + totalWait / 10.0);
            writer.println("-------------------------------");
            writer.println("----------GanttChart-----------");
            for (Integer t : GanttChart) {
                writer.print(t + "|");
            }
            writer.close();
            Quantum = Quantum + 10;
        }
        writerAllrun.close();
    }
}