package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActiviteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Activite getActiviteSample1() {
        return new Activite().id(1L).numActivite(1).typeA("typeA1").description("description1").createur("createur1").visible(1);
    }

    public static Activite getActiviteSample2() {
        return new Activite().id(2L).numActivite(2).typeA("typeA2").description("description2").createur("createur2").visible(2);
    }

    public static Activite getActiviteRandomSampleGenerator() {
        return new Activite()
            .id(longCount.incrementAndGet())
            .numActivite(intCount.incrementAndGet())
            .typeA(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createur(UUID.randomUUID().toString())
            .visible(intCount.incrementAndGet());
    }
}
