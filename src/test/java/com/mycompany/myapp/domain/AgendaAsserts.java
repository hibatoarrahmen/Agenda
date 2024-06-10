package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AgendaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgendaAllPropertiesEquals(Agenda expected, Agenda actual) {
        assertAgendaAutoGeneratedPropertiesEquals(expected, actual);
        assertAgendaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgendaAllUpdatablePropertiesEquals(Agenda expected, Agenda actual) {
        assertAgendaUpdatableFieldsEquals(expected, actual);
        assertAgendaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgendaAutoGeneratedPropertiesEquals(Agenda expected, Agenda actual) {
        assertThat(expected)
            .as("Verify Agenda auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgendaUpdatableFieldsEquals(Agenda expected, Agenda actual) {
        assertThat(expected)
            .as("Verify Agenda relevant properties")
            .satisfies(e -> assertThat(e.getNumAgenda()).as("check numAgenda").isEqualTo(actual.getNumAgenda()))
            .satisfies(e -> assertThat(e.getDateCreation()).as("check dateCreation").isEqualTo(actual.getDateCreation()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAgendaUpdatableRelationshipsEquals(Agenda expected, Agenda actual) {
        assertThat(expected)
            .as("Verify Agenda relationships")
            .satisfies(e -> assertThat(e.getActivite()).as("check activite").isEqualTo(actual.getActivite()));
    }
}
