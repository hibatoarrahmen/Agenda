package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteTestSamples.*;
import static com.mycompany.myapp.domain.AgendaTestSamples.*;
import static com.mycompany.myapp.domain.AlerteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActiviteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activite.class);
        Activite activite1 = getActiviteSample1();
        Activite activite2 = new Activite();
        assertThat(activite1).isNotEqualTo(activite2);

        activite2.setId(activite1.getId());
        assertThat(activite1).isEqualTo(activite2);

        activite2 = getActiviteSample2();
        assertThat(activite1).isNotEqualTo(activite2);
    }

    @Test
    void agendaTest() throws Exception {
        Activite activite = getActiviteRandomSampleGenerator();
        Agenda agendaBack = getAgendaRandomSampleGenerator();

        activite.addAgenda(agendaBack);
        assertThat(activite.getAgenda()).containsOnly(agendaBack);
        assertThat(agendaBack.getActivite()).isEqualTo(activite);

        activite.removeAgenda(agendaBack);
        assertThat(activite.getAgenda()).doesNotContain(agendaBack);
        assertThat(agendaBack.getActivite()).isNull();

        activite.agenda(new HashSet<>(Set.of(agendaBack)));
        assertThat(activite.getAgenda()).containsOnly(agendaBack);
        assertThat(agendaBack.getActivite()).isEqualTo(activite);

        activite.setAgenda(new HashSet<>());
        assertThat(activite.getAgenda()).doesNotContain(agendaBack);
        assertThat(agendaBack.getActivite()).isNull();
    }

    @Test
    void alerteTest() throws Exception {
        Activite activite = getActiviteRandomSampleGenerator();
        Alerte alerteBack = getAlerteRandomSampleGenerator();

        activite.setAlerte(alerteBack);
        assertThat(activite.getAlerte()).isEqualTo(alerteBack);

        activite.alerte(null);
        assertThat(activite.getAlerte()).isNull();
    }
}
