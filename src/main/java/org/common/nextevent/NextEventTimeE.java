package org.common.nextevent;

import java.util.Random;

public class NextEventTimeE {
    //Generate next time(interval) based on exponential distribution
    double lmda;
    Random random;

    public NextEventTimeE(double LAMBDA, int Seed) {
        lmda = LAMBDA;
        random = new Random();
        random.setSeed(Seed);
    }

    public double getNextEventTime(){
        return -Math.log(1 - random.nextDouble())/lmda;
    }
}
