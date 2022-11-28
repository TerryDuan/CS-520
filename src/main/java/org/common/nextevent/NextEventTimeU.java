package org.common.nextevent;

import java.util.Random;

public class NextEventTimeU {
    //Generate next time(interval) based on uniform distribution
    Random random;
    double min;
    double max;

    public NextEventTimeU (double MIN, double MAX){
        min = MIN;
        max = MAX;
        random = new Random();
        random.setSeed(19);
    }

    public double getNextEventTime(){
        return min + random.nextDouble()*(max - min);
    }

}
