package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AgendaDeptAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AgendaDept;
import com.mycompany.myapp.repository.AgendaDeptRepository;
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
 * Integration tests for the {@link AgendaDeptResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgendaDeptResourceIT {

    private static final Integer DEFAULT_NUM_AGENDA = 1;
    private static final Integer UPDATED_NUM_AGENDA = 2;

    private static final LocalDate DEFAULT_DATE_MAJ = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MAJ = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/agenda-depts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgendaDeptRepository agendaDeptRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgendaDeptMockMvc;

    private AgendaDept agendaDept;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgendaDept createEntity(EntityManager em) {
        AgendaDept agendaDept = new AgendaDept().numAgenda(DEFAULT_NUM_AGENDA).dateMAJ(DEFAULT_DATE_MAJ);
        return agendaDept;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgendaDept createUpdatedEntity(EntityManager em) {
        AgendaDept agendaDept = new AgendaDept().numAgenda(UPDATED_NUM_AGENDA).dateMAJ(UPDATED_DATE_MAJ);
        return agendaDept;
    }

    @BeforeEach
    public void initTest() {
        agendaDept = createEntity(em);
    }

    @Test
    @Transactional
    void createAgendaDept() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgendaDept
        var returnedAgendaDept = om.readValue(
            restAgendaDeptMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaDept)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgendaDept.class
        );

        // Validate the AgendaDept in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgendaDeptUpdatableFieldsEquals(returnedAgendaDept, getPersistedAgendaDept(returnedAgendaDept));
    }

    @Test
    @Transactional
    void createAgendaDeptWithExistingId() throws Exception {
        // Create the AgendaDept with an existing ID
        agendaDept.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaDeptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaDept)))
            .andExpect(status().isBadRequest());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgendaDepts() throws Exception {
        // Initialize the database
        agendaDeptRepository.saveAndFlush(agendaDept);

        // Get all the agendaDeptList
        restAgendaDeptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agendaDept.getId().intValue())))
            .andExpect(jsonPath("$.[*].numAgenda").value(hasItem(DEFAULT_NUM_AGENDA)))
            .andExpect(jsonPath("$.[*].dateMAJ").value(hasItem(DEFAULT_DATE_MAJ.toString())));
    }

    @Test
    @Transactional
    void getAgendaDept() throws Exception {
        // Initialize the database
        agendaDeptRepository.saveAndFlush(agendaDept);

        // Get the agendaDept
        restAgendaDeptMockMvc
            .perform(get(ENTITY_API_URL_ID, agendaDept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agendaDept.getId().intValue()))
            .andExpect(jsonPath("$.numAgenda").value(DEFAULT_NUM_AGENDA))
            .andExpect(jsonPath("$.dateMAJ").value(DEFAULT_DATE_MAJ.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAgendaDept() throws Exception {
        // Get the agendaDept
        restAgendaDeptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgendaDept() throws Exception {
        // Initialize the database
        agendaDeptRepository.saveAndFlush(agendaDept);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agendaDept
        AgendaDept updatedAgendaDept = agendaDeptRepository.findById(agendaDept.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgendaDept are not directly saved in db
        em.detach(updatedAgendaDept);
        updatedAgendaDept.numAgenda(UPDATED_NUM_AGENDA).dateMAJ(UPDATED_DATE_MAJ);

        restAgendaDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgendaDept.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgendaDept))
            )
            .andExpect(status().isOk());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgendaDeptToMatchAllProperties(updatedAgendaDept);
    }

    @Test
    @Transactional
    void putNonExistingAgendaDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaDept.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agendaDept.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgendaDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaDeptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agendaDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgendaDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaDeptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agendaDept)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgendaDeptWithPatch() throws Exception {
        // Initialize the database
        agendaDeptRepository.saveAndFlush(agendaDept);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agendaDept using partial update
        AgendaDept partialUpdatedAgendaDept = new AgendaDept();
        partialUpdatedAgendaDept.setId(agendaDept.getId());

        partialUpdatedAgendaDept.numAgenda(UPDATED_NUM_AGENDA);

        restAgendaDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgendaDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgendaDept))
            )
            .andExpect(status().isOk());

        // Validate the AgendaDept in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgendaDeptUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgendaDept, agendaDept),
            getPersistedAgendaDept(agendaDept)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgendaDeptWithPatch() throws Exception {
        // Initialize the database
        agendaDeptRepository.saveAndFlush(agendaDept);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agendaDept using partial update
        AgendaDept partialUpdatedAgendaDept = new AgendaDept();
        partialUpdatedAgendaDept.setId(agendaDept.getId());

        partialUpdatedAgendaDept.numAgenda(UPDATED_NUM_AGENDA).dateMAJ(UPDATED_DATE_MAJ);

        restAgendaDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgendaDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgendaDept))
            )
            .andExpect(status().isOk());

        // Validate the AgendaDept in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgendaDeptUpdatableFieldsEquals(partialUpdatedAgendaDept, getPersistedAgendaDept(partialUpdatedAgendaDept));
    }

    @Test
    @Transactional
    void patchNonExistingAgendaDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaDept.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agendaDept.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agendaDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgendaDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaDeptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agendaDept))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgendaDept() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agendaDept.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaDeptMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agendaDept)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgendaDept in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgendaDept() throws Exception {
        // Initialize the database
        agendaDeptRepository.saveAndFlush(agendaDept);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agendaDept
        restAgendaDeptMockMvc
            .perform(delete(ENTITY_API_URL_ID, agendaDept.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agendaDeptRepository.count();
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

    protected AgendaDept getPersistedAgendaDept(AgendaDept agendaDept) {
        return agendaDeptRepository.findById(agendaDept.getId()).orElseThrow();
    }

    protected void assertPersistedAgendaDeptToMatchAllProperties(AgendaDept expectedAgendaDept) {
        assertAgendaDeptAllPropertiesEquals(expectedAgendaDept, getPersistedAgendaDept(expectedAgendaDept));
    }

    protected void assertPersistedAgendaDeptToMatchUpdatableProperties(AgendaDept expectedAgendaDept) {
        assertAgendaDeptAllUpdatablePropertiesEquals(expectedAgendaDept, getPersistedAgendaDept(expectedAgendaDept));
    }
}
