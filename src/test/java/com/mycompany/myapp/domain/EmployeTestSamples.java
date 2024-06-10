package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Employe getEmployeSample1() {
        return new Employe().id(1L).numEmploye(1).nom("nom1").prenom("prenom1").telIntern("telIntern1").email("email1").niveau(1);
    }

    public static Employe getEmployeSample2() {
        return new Employe().id(2L).numEmploye(2).nom("nom2").prenom("prenom2").telIntern("telIntern2").email("email2").niveau(2);
    }

    public static Employe getEmployeRandomSampleGenerator() {
        return new Employe()
            .id(longCount.incrementAndGet())
            .numEmploye(intCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .telIntern(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .niveau(intCount.incrementAndGet());
    }
}
