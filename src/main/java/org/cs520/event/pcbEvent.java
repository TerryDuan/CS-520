package org.cs520.event;

import org.cs520.pcb.*;
import org.common.nextevent.*;

public class pcbEvent extends event  {
    pcb processBlock;

    public pcbEvent(int t, String Type, pcb p) {
        super(t, Type);
        processBlock = p;
        EventDetails.put("ProcessID", processBlock.getProcessID());
    }

    public pcb getProcess(){
        return processBlock;
    }
    public pcbEvent execute(){
        //tailored method for pcb(event), each pcb will be executed based on type (IO or CPU)
        //return next event for simulator to append to the EventDLL in main function
        if (EventType == "IO"){
            //if current event is IO event, we create next CPU event for this processID and add 60ms for i/o execution
            processBlock.setStatus("Ready"); //always ready after IO
            eventTime = 60;
            pcbEvent nextEvent = new pcbEvent(ts + eventTime, "CPU", processBlock);//add 60s for IO time
            return nextEvent;
        }else if (EventType == "CPU"){
            //if current event is CPU event, we get a random number for CPU burst time and create a new IO event or Complete Event
            processBlock.setStatus("Ready");
            int execT = processBlock.getExecutionTime();
            int ioBurstT = processBlock.getNextIOInterval();
            eventTime = ioBurstT;
            execT = execT - eventTime;
            if (execT > 0){
                processBlock.setExecutionTime(execT);
                pcbEvent nextEvent = new pcbEvent(ts + eventTime, "IO", processBlock);
                return nextEvent;
            }else{
                //process will complete before next IO request
                processBlock.setStatus("Complete");
                processBlock.setExecutionTime(0);
                pcbEvent nextEvent = new pcbEvent(ts + eventTime, "IO", processBlock);
                return nextEvent;
            }
        }else{
            processBlock.setStatus("Complete");
            pcbEvent nextEvent = new pcbEvent(ts, "IO", processBlock);
            return nextEvent;
        }


    }
}
