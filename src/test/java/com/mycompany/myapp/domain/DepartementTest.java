package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AgendaDeptTestSamples.*;
import static com.mycompany.myapp.domain.DepartementTestSamples.*;
import static com.mycompany.myapp.domain.EmployeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepartementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Departement.class);
        Departement departement1 = getDepartementSample1();
        Departement departement2 = new Departement();
        assertThat(departement1).isNotEqualTo(departement2);

        departement2.setId(departement1.getId());
        assertThat(departement1).isEqualTo(departement2);

        departement2 = getDepartementSample2();
        assertThat(departement1).isNotEqualTo(departement2);
    }

    @Test
    void agendaDeptTest() throws Exception {
        Departement departement = getDepartementRandomSampleGenerator();
        AgendaDept agendaDeptBack = getAgendaDeptRandomSampleGenerator();

        departement.setAgendaDept(agendaDeptBack);
        assertThat(departement.getAgendaDept()).isEqualTo(agendaDeptBack);

        departement.agendaDept(null);
        assertThat(departement.getAgendaDept()).isNull();
    }

    @Test
    void employeTest() throws Exception {
        Departement departement = getDepartementRandomSampleGenerator();
        Employe employeBack = getEmployeRandomSampleGenerator();

        departement.setEmploye(employeBack);
        assertThat(departement.getEmploye()).isEqualTo(employeBack);

        departement.employe(null);
        assertThat(departement.getEmploye()).isNull();
    }
}
