package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AgendaDeptTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AgendaDept getAgendaDeptSample1() {
        return new AgendaDept().id(1L).numAgenda(1);
    }

    public static AgendaDept getAgendaDeptSample2() {
        return new AgendaDept().id(2L).numAgenda(2);
    }

    public static AgendaDept getAgendaDeptRandomSampleGenerator() {
        return new AgendaDept().id(longCount.incrementAndGet()).numAgenda(intCount.incrementAndGet());
    }
}
