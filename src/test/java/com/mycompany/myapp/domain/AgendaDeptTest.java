package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteDeptTestSamples.*;
import static com.mycompany.myapp.domain.AgendaDeptTestSamples.*;
import static com.mycompany.myapp.domain.DepartementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgendaDeptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgendaDept.class);
        AgendaDept agendaDept1 = getAgendaDeptSample1();
        AgendaDept agendaDept2 = new AgendaDept();
        assertThat(agendaDept1).isNotEqualTo(agendaDept2);

        agendaDept2.setId(agendaDept1.getId());
        assertThat(agendaDept1).isEqualTo(agendaDept2);

        agendaDept2 = getAgendaDeptSample2();
        assertThat(agendaDept1).isNotEqualTo(agendaDept2);
    }

    @Test
    void activiteDeptTest() throws Exception {
        AgendaDept agendaDept = getAgendaDeptRandomSampleGenerator();
        ActiviteDept activiteDeptBack = getActiviteDeptRandomSampleGenerator();

        agendaDept.setActiviteDept(activiteDeptBack);
        assertThat(agendaDept.getActiviteDept()).isEqualTo(activiteDeptBack);

        agendaDept.activiteDept(null);
        assertThat(agendaDept.getActiviteDept()).isNull();
    }

    @Test
    void departementTest() throws Exception {
        AgendaDept agendaDept = getAgendaDeptRandomSampleGenerator();
        Departement departementBack = getDepartementRandomSampleGenerator();

        agendaDept.setDepartement(departementBack);
        assertThat(agendaDept.getDepartement()).isEqualTo(departementBack);
        assertThat(departementBack.getAgendaDept()).isEqualTo(agendaDept);

        agendaDept.departement(null);
        assertThat(agendaDept.getDepartement()).isNull();
        assertThat(departementBack.getAgendaDept()).isNull();
    }
}
