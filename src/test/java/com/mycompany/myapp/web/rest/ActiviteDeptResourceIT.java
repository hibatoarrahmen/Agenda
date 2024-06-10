package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ActiviteDeptAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ActiviteDept;
import com.mycompany.myapp.repository.ActiviteDeptRepository;
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
 * Integration tests for the {@link ActiviteDeptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActiviteDeptResourceIT {

    private static final Integer DEFAULT_NUM_ACT = 1;
    private static final Integer UPDATED_NUM_ACT = 2;

    private static final String DEFAULT_TYPE_D = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_D = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPT = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/activite-depts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActiviteDeptRepository activiteDeptRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiviteDeptMockMvc;

    private ActiviteDept activiteDept;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActiviteDept createEntity(EntityManager em) {
        ActiviteDept activiteDept = new ActiviteDept()
            .numAct(DEFAULT_NUM_ACT)
            .typeD(DEFAULT_TYPE_D)
            .descript(DEFAULT_DESCRIPT)
            .dateAct(DEFAULT_DATE_ACT)
            .hDebut(DEFAULT_H_DEBUT)
            .hFin(DEFAULT_H_FIN)
            .dateCreation(DEFAULT_DATE_CREATION)
            .createur(DEFAULT_CREATEUR);
        return activiteDept;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActiviteDept createUpdatedEntity(EntityManager em) {
        ActiviteDept activiteDept = new ActiviteDept()
            .numAct(UPDATED_NUM_ACT)
            .typeD(UPDATED_TYPE_D)
            .descript(UPDATED_DESCRIPT)
            .dateAct(UPDATED_DATE_ACT)
            .hDebut(UPDATED_H_DEBUT)
            .hFin(UPDATED_H_FIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .createur(UPDATED_CREATEUR);
        return activiteDept;
    }

    @BeforeEach
    public void initTest() {
        activiteDept = createEntity(em);
    }

    @Test
    @Transactional
    void createActiviteDept() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ActiviteDept
        var returnedActiviteDept = om.readValue(
            restActiviteDeptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteDept)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ActiviteDept.class
        );

        // Validate the ActiviteDept in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertActiviteDeptUpdatableFieldsEquals(returnedActiviteDept, getPersistedActiviteDept(returnedActiviteDept));
    }

    @Test
    @Transactional
    void createActiviteDeptWithExistingId() throws Exception {
        // Create the ActiviteDept with an existing ID
        activiteDept.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiviteDeptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteDept)))
            .andExpect(status().isBadRequest());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActiviteDepts() throws Exception {
        // Initialize the database
        activiteDeptRepository.saveAndFlush(activiteDept);

        // Get all the activiteDeptList
        restActiviteDeptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activiteDept.getId().intValue())))
            .andExpect(jsonPath("$.[*].numAct").value(hasItem(DEFAULT_NUM_ACT)))
            .andExpect(jsonPath("$.[*].typeD").value(hasItem(DEFAULT_TYPE_D)))
            .andExpect(jsonPath("$.[*].descript").value(hasItem(DEFAULT_DESCRIPT)))
            .andExpect(jsonPath("$.[*].dateAct").value(hasItem(DEFAULT_DATE_ACT.toString())))
            .andExpect(jsonPath("$.[*].hDebut").value(hasItem(DEFAULT_H_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].hFin").value(hasItem(DEFAULT_H_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].createur").value(hasItem(DEFAULT_CREATEUR)));
    }

    @Test
    @Transactional
    void getActiviteDept() throws Exception {
        // Initialize the database
        activiteDeptRepository.saveAndFlush(activiteDept);

        // Get the activiteDept
        restActiviteDeptMockMvc
            .perform(get(ENTITY_API_URL_ID, activiteDept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activiteDept.getId().intValue()))
            .andExpect(jsonPath("$.numAct").value(DEFAULT_NUM_ACT))
            .andExpect(jsonPath("$.typeD").value(DEFAULT_TYPE_D))
            .andExpect(jsonPath("$.descript").value(DEFAULT_DESCRIPT))
            .andExpect(jsonPath("$.dateAct").value(DEFAULT_DATE_ACT.toString()))
            .andExpect(jsonPath("$.hDebut").value(DEFAULT_H_DEBUT.toString()))
            .andExpect(jsonPath("$.hFin").value(DEFAULT_H_FIN.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.createur").value(DEFAULT_CREATEUR));
    }

    @Test
    @Transactional
    void getNonExistingActiviteDept() throws Exception {
        // Get the activiteDept
        restActiviteDeptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActiviteDept() throws Exception {
        // Initialize the database
        activiteDeptRepository.saveAndFlush(activiteDept);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activiteDept
        ActiviteDept updatedActiviteDept = activiteDeptRepository.findById(activiteDept.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActiviteDept are not directly saved in db
        em.detach(updatedActiviteDept);
        updatedActiviteDept
            .numAct(UPDATED_NUM_ACT)
            .typeD(UPDATED_TYPE_D)
            .descript(UPDATED_DESCRIPT)
            .dateAct(UPDATED_DATE_ACT)
            .hDebut(UPDATED_H_DEBUT)
            .hFin(UPDATED_H_FIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .createur(UPDATED_CREATEUR);

        restActiviteDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActiviteDept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedActiviteDept))
            )
            .andExpect(status().isOk());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedActiviteDeptToMatchAllProperties(updatedActiviteDept);
    }

    @Test
    @Transactional
    void putNonExistingActiviteDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteDept.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activiteDept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activiteDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActiviteDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(activiteDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActiviteDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteDeptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(activiteDept)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActiviteDeptWithPatch() throws Exception {
        // Initialize the database
        activiteDeptRepository.saveAndFlush(activiteDept);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activiteDept using partial update
        ActiviteDept partialUpdatedActiviteDept = new ActiviteDept();
        partialUpdatedActiviteDept.setId(activiteDept.getId());

        partialUpdatedActiviteDept.descript(UPDATED_DESCRIPT).hFin(UPDATED_H_FIN).dateCreation(UPDATED_DATE_CREATION);

        restActiviteDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActiviteDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActiviteDept))
            )
            .andExpect(status().isOk());

        // Validate the ActiviteDept in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiviteDeptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedActiviteDept, activiteDept),
            getPersistedActiviteDept(activiteDept)
        );
    }

    @Test
    @Transactional
    void fullUpdateActiviteDeptWithPatch() throws Exception {
        // Initialize the database
        activiteDeptRepository.saveAndFlush(activiteDept);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the activiteDept using partial update
        ActiviteDept partialUpdatedActiviteDept = new ActiviteDept();
        partialUpdatedActiviteDept.setId(activiteDept.getId());

        partialUpdatedActiviteDept
            .numAct(UPDATED_NUM_ACT)
            .typeD(UPDATED_TYPE_D)
            .descript(UPDATED_DESCRIPT)
            .dateAct(UPDATED_DATE_ACT)
            .hDebut(UPDATED_H_DEBUT)
            .hFin(UPDATED_H_FIN)
            .dateCreation(UPDATED_DATE_CREATION)
            .createur(UPDATED_CREATEUR);

        restActiviteDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActiviteDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedActiviteDept))
            )
            .andExpect(status().isOk());

        // Validate the ActiviteDept in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertActiviteDeptUpdatableFieldsEquals(partialUpdatedActiviteDept, getPersistedActiviteDept(partialUpdatedActiviteDept));
    }

    @Test
    @Transactional
    void patchNonExistingActiviteDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteDept.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activiteDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activiteDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActiviteDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(activiteDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActiviteDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        activiteDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteDeptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(activiteDept)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActiviteDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActiviteDept() throws Exception {
        // Initialize the database
        activiteDeptRepository.saveAndFlush(activiteDept);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the activiteDept
        restActiviteDeptMockMvc
            .perform(delete(ENTITY_API_URL_ID, activiteDept.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return activiteDeptRepository.count();
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

    protected ActiviteDept getPersistedActiviteDept(ActiviteDept activiteDept) {
        return activiteDeptRepository.findById(activiteDept.getId()).orElseThrow();
    }

    protected void assertPersistedActiviteDeptToMatchAllProperties(ActiviteDept expectedActiviteDept) {
        assertActiviteDeptAllPropertiesEquals(expectedActiviteDept, getPersistedActiviteDept(expectedActiviteDept));
    }

    protected void assertPersistedActiviteDeptToMatchUpdatableProperties(ActiviteDept expectedActiviteDept) {
        assertActiviteDeptAllUpdatablePropertiesEquals(expectedActiviteDept, getPersistedActiviteDept(expectedActiviteDept));
    }
}
