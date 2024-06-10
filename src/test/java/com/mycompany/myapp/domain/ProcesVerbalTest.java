package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteDeptTestSamples.*;
import static com.mycompany.myapp.domain.ProcesVerbalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcesVerbalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcesVerbal.class);
        ProcesVerbal procesVerbal1 = getProcesVerbalSample1();
        ProcesVerbal procesVerbal2 = new ProcesVerbal();
        assertThat(procesVerbal1).isNotEqualTo(procesVerbal2);

        procesVerbal2.setId(procesVerbal1.getId());
        assertThat(procesVerbal1).isEqualTo(procesVerbal2);

        procesVerbal2 = getProcesVerbalSample2();
        assertThat(procesVerbal1).isNotEqualTo(procesVerbal2);
    }

    @Test
    void activiteDeptTest() throws Exception {
        ProcesVerbal procesVerbal = getProcesVerbalRandomSampleGenerator();
        ActiviteDept activiteDeptBack = getActiviteDeptRandomSampleGenerator();

        procesVerbal.setActiviteDept(activiteDeptBack);
        assertThat(procesVerbal.getActiviteDept()).isEqualTo(activiteDeptBack);
        assertThat(activiteDeptBack.getProcesVerbal()).isEqualTo(procesVerbal);

        procesVerbal.activiteDept(null);
        assertThat(procesVerbal.getActiviteDept()).isNull();
        assertThat(activiteDeptBack.getProcesVerbal()).isNull();
    }
}
