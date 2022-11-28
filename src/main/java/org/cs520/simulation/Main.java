package org.cs520.simulation;

import org.common.nextevent.*;
import org.cs520.pcb.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Start Simulation");
        //NextEventTimeE eventTimeGenerator = new NextEventTimeE(0.03333);
        //double nextTime = eventTimeGenerator.getNextEventTime();
        //System.out.println(nextTime); //39.491732258129765
        NextEventTimeU eventTimeGeneratorU = new NextEventTimeU(2,4);
        double nextUTime = eventTimeGeneratorU.getNextEventTime();
        System.out.println(nextUTime); //3.4649813252059607
        pcb sampleProcess = new pcb("0x00", (int) nextUTime*60000 ,0.03333 ); //0.0333 lambda --> avg 30
        System.out.println(sampleProcess.getStatus());
        System.out.println(sampleProcess.getMeanIoInterval());
        System.out.println(sampleProcess.getNextIOInterval());
        System.out.println(sampleProcess.getNextIOInterval());
        System.out.println(sampleProcess.getNextIOInterval());
        System.out.println(sampleProcess.getExecutionTime());
    }
}