package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteDeptTestSamples.*;
import static com.mycompany.myapp.domain.AgendaDeptTestSamples.*;
import static com.mycompany.myapp.domain.EmployeTestSamples.*;
import static com.mycompany.myapp.domain.ProcesVerbalTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActiviteDeptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActiviteDept.class);
        ActiviteDept activiteDept1 = getActiviteDeptSample1();
        ActiviteDept activiteDept2 = new ActiviteDept();
        assertThat(activiteDept1).isNotEqualTo(activiteDept2);

        activiteDept2.setId(activiteDept1.getId());
        assertThat(activiteDept1).isEqualTo(activiteDept2);

        activiteDept2 = getActiviteDeptSample2();
        assertThat(activiteDept1).isNotEqualTo(activiteDept2);
    }

    @Test
    void procesVerbalTest() throws Exception {
        ActiviteDept activiteDept = getActiviteDeptRandomSampleGenerator();
        ProcesVerbal procesVerbalBack = getProcesVerbalRandomSampleGenerator();

        activiteDept.setProcesVerbal(procesVerbalBack);
        assertThat(activiteDept.getProcesVerbal()).isEqualTo(procesVerbalBack);

        activiteDept.procesVerbal(null);
        assertThat(activiteDept.getProcesVerbal()).isNull();
    }

    @Test
    void agendaDeptTest() throws Exception {
        ActiviteDept activiteDept = getActiviteDeptRandomSampleGenerator();
        AgendaDept agendaDeptBack = getAgendaDeptRandomSampleGenerator();

        activiteDept.addAgendaDept(agendaDeptBack);
        assertThat(activiteDept.getAgendaDepts()).containsOnly(agendaDeptBack);
        assertThat(agendaDeptBack.getActiviteDept()).isEqualTo(activiteDept);

        activiteDept.removeAgendaDept(agendaDeptBack);
        assertThat(activiteDept.getAgendaDepts()).doesNotContain(agendaDeptBack);
        assertThat(agendaDeptBack.getActiviteDept()).isNull();

        activiteDept.agendaDepts(new HashSet<>(Set.of(agendaDeptBack)));
        assertThat(activiteDept.getAgendaDepts()).containsOnly(agendaDeptBack);
        assertThat(agendaDeptBack.getActiviteDept()).isEqualTo(activiteDept);

        activiteDept.setAgendaDepts(new HashSet<>());
        assertThat(activiteDept.getAgendaDepts()).doesNotContain(agendaDeptBack);
        assertThat(agendaDeptBack.getActiviteDept()).isNull();
    }

    @Test
    void employeTest() throws Exception {
        ActiviteDept activiteDept = getActiviteDeptRandomSampleGenerator();
        Employe employeBack = getEmployeRandomSampleGenerator();

        activiteDept.addEmploye(employeBack);
        assertThat(activiteDept.getEmployes()).containsOnly(employeBack);
        assertThat(employeBack.getActiviteDepts()).containsOnly(activiteDept);

        activiteDept.removeEmploye(employeBack);
        assertThat(activiteDept.getEmployes()).doesNotContain(employeBack);
        assertThat(employeBack.getActiviteDepts()).doesNotContain(activiteDept);

        activiteDept.employes(new HashSet<>(Set.of(employeBack)));
        assertThat(activiteDept.getEmployes()).containsOnly(employeBack);
        assertThat(employeBack.getActiviteDepts()).containsOnly(activiteDept);

        activiteDept.setEmployes(new HashSet<>());
        assertThat(activiteDept.getEmployes()).doesNotContain(employeBack);
        assertThat(employeBack.getActiviteDepts()).doesNotContain(activiteDept);
    }
}
