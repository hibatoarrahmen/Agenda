package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ActiviteAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActiviteAllPropertiesEquals(Activite expected, Activite actual) {
        assertActiviteAutoGeneratedPropertiesEquals(expected, actual);
        assertActiviteAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActiviteAllUpdatablePropertiesEquals(Activite expected, Activite actual) {
        assertActiviteUpdatableFieldsEquals(expected, actual);
        assertActiviteUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActiviteAutoGeneratedPropertiesEquals(Activite expected, Activite actual) {
        assertThat(expected)
            .as("Verify Activite auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActiviteUpdatableFieldsEquals(Activite expected, Activite actual) {
        assertThat(expected)
            .as("Verify Activite relevant properties")
            .satisfies(e -> assertThat(e.getNumActivite()).as("check numActivite").isEqualTo(actual.getNumActivite()))
            .satisfies(e -> assertThat(e.getTypeA()).as("check typeA").isEqualTo(actual.getTypeA()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getDateAct()).as("check dateAct").isEqualTo(actual.getDateAct()))
            .satisfies(e -> assertThat(e.gethDebut()).as("check hDebut").isEqualTo(actual.gethDebut()))
            .satisfies(e -> assertThat(e.gethFin()).as("check hFin").isEqualTo(actual.gethFin()))
            .satisfies(e -> assertThat(e.getDateCreation()).as("check dateCreation").isEqualTo(actual.getDateCreation()))
            .satisfies(e -> assertThat(e.getCreateur()).as("check createur").isEqualTo(actual.getCreateur()))
            .satisfies(e -> assertThat(e.getVisible()).as("check visible").isEqualTo(actual.getVisible()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertActiviteUpdatableRelationshipsEquals(Activite expected, Activite actual) {
        assertThat(expected)
            .as("Verify Activite relationships")
            .satisfies(e -> assertThat(e.getAlerte()).as("check alerte").isEqualTo(actual.getAlerte()));
    }
}
