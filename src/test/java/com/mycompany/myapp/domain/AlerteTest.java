package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ActiviteTestSamples.*;
import static com.mycompany.myapp.domain.AlerteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlerteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alerte.class);
        Alerte alerte1 = getAlerteSample1();
        Alerte alerte2 = new Alerte();
        assertThat(alerte1).isNotEqualTo(alerte2);

        alerte2.setId(alerte1.getId());
        assertThat(alerte1).isEqualTo(alerte2);

        alerte2 = getAlerteSample2();
        assertThat(alerte1).isNotEqualTo(alerte2);
    }

    @Test
    void activiteTest() throws Exception {
        Alerte alerte = getAlerteRandomSampleGenerator();
        Activite activiteBack = getActiviteRandomSampleGenerator();

        alerte.addActivite(activiteBack);
        assertThat(alerte.getActivites()).containsOnly(activiteBack);
        assertThat(activiteBack.getAlerte()).isEqualTo(alerte);

        alerte.removeActivite(activiteBack);
        assertThat(alerte.getActivites()).doesNotContain(activiteBack);
        assertThat(activiteBack.getAlerte()).isNull();

        alerte.activites(new HashSet<>(Set.of(activiteBack)));
        assertThat(alerte.getActivites()).containsOnly(activiteBack);
        assertThat(activiteBack.getAlerte()).isEqualTo(alerte);

        alerte.setActivites(new HashSet<>());
        assertThat(alerte.getActivites()).doesNotContain(activiteBack);
        assertThat(activiteBack.getAlerte()).isNull();
    }
}
