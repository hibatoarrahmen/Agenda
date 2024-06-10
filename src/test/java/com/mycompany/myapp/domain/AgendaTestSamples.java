package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AgendaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Agenda getAgendaSample1() {
        return new Agenda().id(1L).numAgenda(1);
    }

    public static Agenda getAgendaSample2() {
        return new Agenda().id(2L).numAgenda(2);
    }

    public static Agenda getAgendaRandomSampleGenerator() {
        return new Agenda().id(longCount.incrementAndGet()).numAgenda(intCount.incrementAndGet());
    }
}
