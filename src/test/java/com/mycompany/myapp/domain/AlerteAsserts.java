package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AlerteAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlerteAllPropertiesEquals(Alerte expected, Alerte actual) {
        assertAlerteAutoGeneratedPropertiesEquals(expected, actual);
        assertAlerteAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlerteAllUpdatablePropertiesEquals(Alerte expected, Alerte actual) {
        assertAlerteUpdatableFieldsEquals(expected, actual);
        assertAlerteUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlerteAutoGeneratedPropertiesEquals(Alerte expected, Alerte actual) {
        assertThat(expected)
            .as("Verify Alerte auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlerteUpdatableFieldsEquals(Alerte expected, Alerte actual) {
        assertThat(expected)
            .as("Verify Alerte relevant properties")
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getDelais()).as("check delais").isEqualTo(actual.getDelais()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAlerteUpdatableRelationshipsEquals(Alerte expected, Alerte actual) {}
}
