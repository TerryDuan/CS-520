package org.cs520.pcb;

import org.common.nextevent.*;

public class pcb {
    //This is a pcb class to store information for a process
    String ProcessID;
    int ExecutionTime;
    double ioIntervalLambda;
    String Status; //for this simulation, pcb status only has Ready/Complete, running status is irrelevant for Queue based simulation

    NextEventTimeU execTimeGeneratorU;
    NextEventTimeE ioBurstTimeGenerator;

    public pcb(String id, int execT, double ioT){
        ProcessID = id;
        ExecutionTime = execT;
        ioIntervalLambda = ioT;
        Status = "ReadyCPU";
        //We need to initialize the random time generator for each PCB (process)'s IO Burst
        ioBurstTimeGenerator = new NextEventTimeE(ioIntervalLambda);
    }

    public void setStatus(String newStatus){
        Status = newStatus;
    }

    public void setExecutionTime(int newT){
        ExecutionTime = newT;
    }

    public int getNextIOInterval(){
        return (int) ioBurstTimeGenerator.getNextEventTime();
    }

    public double getMeanIoInterval(){
        return 1/ioIntervalLambda;
    }

    public int getExecutionTime(){
        return ExecutionTime;
    }

    public String getStatus(){
        return Status;
    }

    public String getProcessID(){
        return ProcessID;
    }

}
