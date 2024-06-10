package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteDeptTestSamples.*;
import static com.mycompany.myapp.domain.AgendaTestSamples.*;
import static com.mycompany.myapp.domain.DepartementTestSamples.*;
import static com.mycompany.myapp.domain.EmployeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EmployeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employe.class);
        Employe employe1 = getEmployeSample1();
        Employe employe2 = new Employe();
        assertThat(employe1).isNotEqualTo(employe2);

        employe2.setId(employe1.getId());
        assertThat(employe1).isEqualTo(employe2);

        employe2 = getEmployeSample2();
        assertThat(employe1).isNotEqualTo(employe2);
    }

    @Test
    void agendaTest() throws Exception {
        Employe employe = getEmployeRandomSampleGenerator();
        Agenda agendaBack = getAgendaRandomSampleGenerator();

        employe.setAgenda(agendaBack);
        assertThat(employe.getAgenda()).isEqualTo(agendaBack);

        employe.agenda(null);
        assertThat(employe.getAgenda()).isNull();
    }

    @Test
    void departementTest() throws Exception {
        Employe employe = getEmployeRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        employe.addDepartement(departementBack);
        assertThat(employe.getDepartements()).containsOnly(departementBack);
        assertThat(departementBack.getEmploye()).isEqualTo(employe);

        employe.removeDepartement(departementBack);
        assertThat(employe.getDepartements()).doesNotContain(departementBack);
        assertThat(departementBack.getEmploye()).isNull();

        employe.departements(new HashSet<>(Set.of(departementBack)));
        assertThat(employe.getDepartements()).containsOnly(departementBack);
        assertThat(departementBack.getEmploye()).isEqualTo(employe);

        employe.setDepartements(new HashSet<>());
        assertThat(employe.getDepartements()).doesNotContain(departementBack);
        assertThat(departementBack.getEmploye()).isNull();
    }

    @Test
    void activiteDeptTest() throws Exception {
        Employe employe = getEmployeRandomSampleGenerator();
        ActiviteDept activiteDeptBack = getActiviteDeptRandomSampleGenerator();

        employe.addActiviteDept(activiteDeptBack);
        assertThat(employe.getActiviteDepts()).containsOnly(activiteDeptBack);

        employe.removeActiviteDept(activiteDeptBack);
        assertThat(employe.getActiviteDepts()).doesNotContain(activiteDeptBack);

        employe.activiteDepts(new HashSet<>(Set.of(activiteDeptBack)));
        assertThat(employe.getActiviteDepts()).containsOnly(activiteDeptBack);

        employe.setActiviteDepts(new HashSet<>());
        assertThat(employe.getActiviteDepts()).doesNotContain(activiteDeptBack);
    }
}
