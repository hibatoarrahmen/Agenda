package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlerteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Alerte getAlerteSample1() {
        return new Alerte().id(1L).type("type1").delais(1);
    }

    public static Alerte getAlerteSample2() {
        return new Alerte().id(2L).type("type2").delais(2);
    }

    public static Alerte getAlerteRandomSampleGenerator() {
        return new Alerte().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString()).delais(intCount.incrementAndGet());
    }
}
