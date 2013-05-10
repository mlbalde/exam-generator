package main;

import java.util.Calendar;
import java.util.Random;

/**
 * Singleton to generate the randomness
 * @author visoft
 */
public class RandomSource {
    private Random rnd;
    private static RandomSource RS=new RandomSource();
    private RandomSource(){
        rnd=new Random(Calendar.getInstance().getTimeInMillis());
    }
    public static Random get(){
        return RS.rnd;
    }
}
