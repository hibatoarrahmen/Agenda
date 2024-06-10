package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ActiviteAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Activite;
import com.mycompany.myapp.repository.ActiviteRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ActiviteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActiviteResourceIT {

    private static final Integer DEFAULT_NUM_ACTIVITE = 1;
    private static final Integer UPDATED_NUM_ACTIVITE = 2;

    private static final String DEFAULT_TYPE_A = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_A = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ACT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ACT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_H_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_H_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_H_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_H_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATEUR = "AAAAAAAAAA";
    private static final String UPDATED_CREATEUR = "BBBBBBBBBB";

    private static final Integer DEFAULT_VISIBLE = 1;
    private static final Integer UPDATED_VISIBLE = 2;

    private static final String ENTITY_API_URL = "/api/activites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActiviteRepository activiteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiviteMockMvc;

    private Activite activite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createEntity(EntityManager em) {
        Activite activite = new Activite()
            .numActivite(DEFAULT_NUM_ACTIVITE)
            .typeA(DEFAULT_TYPE_A)
            .description(DEFAULT_DESCRIPTION)
            .dateAct(DEFAULT_DATE_ACT)
            .hDebut(DEFAULT_H_DEBUT)
            .hFin(DEFAULT_H_FIN)
            .dateCreation(DEFAULT_DATE_CREATION)
            .createur(DEFAULT_CREATEUR)
            .visible(DEFAULT_VISIBLE);
        return activite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createUpdatedEntity(EntityManager em) {
        Activite activite = new Activite()
            .numActivite(UPDATED_NUM_ACTIVITE)
            .typeA(UPDATED_TYPE_A)
            .description(UPDATED_DESCRIPTION)
            .dateAct(UPDATED_DATE_ACT)
            .hDebut(UPDATED_H_DEBUT)
            .hFin(UPDATED_H_FIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .createur(UPDATED_CREATEUR)
            .visible(UPDATED_VISIBLE);
        return activite;
    }

    @BeforeEach
    public void initTest() {
        activite = createEntity(em);
    }

    @Test
    @Transactional
    void createActivite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Activite
        var returnedActivite = om.readValue(
            restActiviteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activite)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Activite.class
        );

        // Validate the Activite in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActiviteUpdatableFieldsEquals(returnedActivite, getPersistedActivite(returnedActivite));
    }

    @Test
    @Transactional
    void createActiviteWithExistingId() throws Exception {
        // Create the Activite with an existing ID
        activite.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activite)))
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivites() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].numActivite").value(hasItem(DEFAULT_NUM_ACTIVITE)))
            .andExpect(jsonPath("$.[*].typeA").value(hasItem(DEFAULT_TYPE_A)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateAct").value(hasItem(DEFAULT_DATE_ACT.toString())))
            .andExpect(jsonPath("$.[*].hDebut").value(hasItem(DEFAULT_H_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].hFin").value(hasItem(DEFAULT_H_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].createur").value(hasItem(DEFAULT_CREATEUR)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE)));
    }

    @Test
    @Transactional
    void getActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get the activite
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL_ID, activite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activite.getId().intValue()))
            .andExpect(jsonPath("$.numActivite").value(DEFAULT_NUM_ACTIVITE))
            .andExpect(jsonPath("$.typeA").value(DEFAULT_TYPE_A))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateAct").value(DEFAULT_DATE_ACT.toString()))
            .andExpect(jsonPath("$.hDebut").value(DEFAULT_H_DEBUT.toString()))
            .andExpect(jsonPath("$.hFin").value(DEFAULT_H_FIN.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.createur").value(DEFAULT_CREATEUR))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE));
    }

    @Test
    @Transactional
    void getNonExistingActivite() throws Exception {
        // Get the activite
        restActiviteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activite
        Activite updatedActivite = activiteRepository.findById(activite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActivite are not directly saved in db
        em.detach(updatedActivite);
        updatedActivite
            .numActivite(UPDATED_NUM_ACTIVITE)
            .typeA(UPDATED_TYPE_A)
            .description(UPDATED_DESCRIPTION)
            .dateAct(UPDATED_DATE_ACT)
            .hDebut(UPDATED_H_DEBUT)
            .hFin(UPDATED_H_FIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .createur(UPDATED_CREATEUR)
            .visible(UPDATED_VISIBLE);

        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActiviteToMatchAllProperties(updatedActivite);
    }

    @Test
    @Transactional
    void putNonExistingActivite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activite.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActiviteWithPatch() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activite using partial update
        Activite partialUpdatedActivite = new Activite();
        partialUpdatedActivite.setId(activite.getId());

        partialUpdatedActivite.description(UPDATED_DESCRIPTION).dateAct(UPDATED_DATE_ACT).visible(UPDATED_VISIBLE);

        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiviteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedActivite, activite), getPersistedActivite(activite));
    }

    @Test
    @Transactional
    void fullUpdateActiviteWithPatch() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activite using partial update
        Activite partialUpdatedActivite = new Activite();
        partialUpdatedActivite.setId(activite.getId());

        partialUpdatedActivite
            .numActivite(UPDATED_NUM_ACTIVITE)
            .typeA(UPDATED_TYPE_A)
            .description(UPDATED_DESCRIPTION)
            .dateAct(UPDATED_DATE_ACT)
            .hDebut(UPDATED_H_DEBUT)
            .hFin(UPDATED_H_FIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .createur(UPDATED_CREATEUR)
            .visible(UPDATED_VISIBLE);

        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiviteUpdatableFieldsEquals(partialUpdatedActivite, getPersistedActivite(partialUpdatedActivite));
    }

    @Test
    @Transactional
    void patchNonExistingActivite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activite in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activite
        restActiviteMockMvc
            .perform(delete(ENTITY_API_URL_ID, activite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activiteRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Activite getPersistedActivite(Activite activite) {
        return activiteRepository.findById(activite.getId()).orElseThrow();
    }

    protected void assertPersistedActiviteToMatchAllProperties(Activite expectedActivite) {
        assertActiviteAllPropertiesEquals(expectedActivite, getPersistedActivite(expectedActivite));
    }

    protected void assertPersistedActiviteToMatchUpdatableProperties(Activite expectedActivite) {
        assertActiviteAllUpdatablePropertiesEquals(expectedActivite, getPersistedActivite(expectedActivite));
    }
}
