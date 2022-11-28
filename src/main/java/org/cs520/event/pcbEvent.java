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

    public pcbEvent execute(){
        //tailored method for pcb(event), each pcb will be executed based on type (IO or CPU)
        //return next event for simulator to append to the EventDLL in main function
        if (EventType == "IO"){
            //if current event is IO event, we create next CPU event for this processID and add 60ms for i/o execution
            processBlock.setStatus("Ready");
            pcbEvent nextEvent = new pcbEvent(ts + 60, "CPU", processBlock);
            return nextEvent;
        }else if (EventType == "CPU"){
            //if current event is CPU event, we get a random number for CPU burst time and create a new IO event or Complete Event
            processBlock.setStatus("Ready");
            int execT = processBlock.getExecutionTime();

            //int executeTime =
            pcbEvent nextEvent = new pcbEvent(ts + 60, "IO", processBlock);
            return nextEvent;
        }else{
            processBlock.setStatus("Complete");
            return null;
        }


    }
}
