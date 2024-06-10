package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActiviteDeptTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ActiviteDept getActiviteDeptSample1() {
        return new ActiviteDept().id(1L).numAct(1).typeD("typeD1").descript("descript1").createur("createur1");
    }

    public static ActiviteDept getActiviteDeptSample2() {
        return new ActiviteDept().id(2L).numAct(2).typeD("typeD2").descript("descript2").createur("createur2");
    }

    public static ActiviteDept getActiviteDeptRandomSampleGenerator() {
        return new ActiviteDept()
            .id(longCount.incrementAndGet())
            .numAct(intCount.incrementAndGet())
            .typeD(UUID.randomUUID().toString())
            .descript(UUID.randomUUID().toString())
            .createur(UUID.randomUUID().toString());
    }
}
