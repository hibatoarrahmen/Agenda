package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcesVerbalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProcesVerbal getProcesVerbalSample1() {
        return new ProcesVerbal().id(1L).numProcesV(1).resum("resum1");
    }

    public static ProcesVerbal getProcesVerbalSample2() {
        return new ProcesVerbal().id(2L).numProcesV(2).resum("resum2");
    }

    public static ProcesVerbal getProcesVerbalRandomSampleGenerator() {
        return new ProcesVerbal()
            .id(longCount.incrementAndGet())
            .numProcesV(intCount.incrementAndGet())
            .resum(UUID.randomUUID().toString());
    }
}
