package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteTestSamples.*;
import static com.mycompany.myapp.domain.AgendaTestSamples.*;
import static com.mycompany.myapp.domain.EmployeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agenda.class);
        Agenda agenda1 = getAgendaSample1();
        Agenda agenda2 = new Agenda();
        assertThat(agenda1).isNotEqualTo(agenda2);

        agenda2.setId(agenda1.getId());
        assertThat(agenda1).isEqualTo(agenda2);

        agenda2 = getAgendaSample2();
        assertThat(agenda1).isNotEqualTo(agenda2);
    }

    @Test
    void activiteTest() throws Exception {
        Agenda agenda = getAgendaRandomSampleGenerator();
        Activite activiteBack = getActiviteRandomSampleGenerator();

        agenda.setActivite(activiteBack);
        assertThat(agenda.getActivite()).isEqualTo(activiteBack);

        agenda.activite(null);
        assertThat(agenda.getActivite()).isNull();
    }

    @Test
    void employeTest() throws Exception {
        Agenda agenda = getAgendaRandomSampleGenerator();
        Employe employeBack = getEmployeRandomSampleGenerator();

        agenda.setEmploye(employeBack);
        assertThat(agenda.getEmploye()).isEqualTo(employeBack);
        assertThat(employeBack.getAgenda()).isEqualTo(agenda);

        agenda.employe(null);
        assertThat(agenda.getEmploye()).isNull();
        assertThat(employeBack.getAgenda()).isNull();
    }
}
