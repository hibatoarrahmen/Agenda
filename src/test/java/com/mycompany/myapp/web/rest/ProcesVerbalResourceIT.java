package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProcesVerbalAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProcesVerbal;
import com.mycompany.myapp.repository.ProcesVerbalRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ProcesVerbalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcesVerbalResourceIT {

    private static final Integer DEFAULT_NUM_PROCES_V = 1;
    private static final Integer UPDATED_NUM_PROCES_V = 2;

    private static final String DEFAULT_RESUM = "AAAAAAAAAA";
    private static final String UPDATED_RESUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/proces-verbals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProcesVerbalRepository procesVerbalRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcesVerbalMockMvc;

    private ProcesVerbal procesVerbal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcesVerbal createEntity(EntityManager em) {
        ProcesVerbal procesVerbal = new ProcesVerbal().numProcesV(DEFAULT_NUM_PROCES_V).resum(DEFAULT_RESUM);
        return procesVerbal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcesVerbal createUpdatedEntity(EntityManager em) {
        ProcesVerbal procesVerbal = new ProcesVerbal().numProcesV(UPDATED_NUM_PROCES_V).resum(UPDATED_RESUM);
        return procesVerbal;
    }

    @BeforeEach
    public void initTest() {
        procesVerbal = createEntity(em);
    }

    @Test
    @Transactional
    void createProcesVerbal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProcesVerbal
        var returnedProcesVerbal = om.readValue(
            restProcesVerbalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(procesVerbal)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProcesVerbal.class
        );

        // Validate the ProcesVerbal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProcesVerbalUpdatableFieldsEquals(returnedProcesVerbal, getPersistedProcesVerbal(returnedProcesVerbal));
    }

    @Test
    @Transactional
    void createProcesVerbalWithExistingId() throws Exception {
        // Create the ProcesVerbal with an existing ID
        procesVerbal.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcesVerbalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(procesVerbal)))
            .andExpect(status().isBadRequest());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcesVerbals() throws Exception {
        // Initialize the database
        procesVerbalRepository.saveAndFlush(procesVerbal);

        // Get all the procesVerbalList
        restProcesVerbalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procesVerbal.getId().intValue())))
            .andExpect(jsonPath("$.[*].numProcesV").value(hasItem(DEFAULT_NUM_PROCES_V)))
            .andExpect(jsonPath("$.[*].resum").value(hasItem(DEFAULT_RESUM)));
    }

    @Test
    @Transactional
    void getProcesVerbal() throws Exception {
        // Initialize the database
        procesVerbalRepository.saveAndFlush(procesVerbal);

        // Get the procesVerbal
        restProcesVerbalMockMvc
            .perform(get(ENTITY_API_URL_ID, procesVerbal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(procesVerbal.getId().intValue()))
            .andExpect(jsonPath("$.numProcesV").value(DEFAULT_NUM_PROCES_V))
            .andExpect(jsonPath("$.resum").value(DEFAULT_RESUM));
    }

    @Test
    @Transactional
    void getNonExistingProcesVerbal() throws Exception {
        // Get the procesVerbal
        restProcesVerbalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcesVerbal() throws Exception {
        // Initialize the database
        procesVerbalRepository.saveAndFlush(procesVerbal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the procesVerbal
        ProcesVerbal updatedProcesVerbal = procesVerbalRepository.findById(procesVerbal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProcesVerbal are not directly saved in db
        em.detach(updatedProcesVerbal);
        updatedProcesVerbal.numProcesV(UPDATED_NUM_PROCES_V).resum(UPDATED_RESUM);

        restProcesVerbalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcesVerbal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProcesVerbal))
            )
            .andExpect(status().isOk());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProcesVerbalToMatchAllProperties(updatedProcesVerbal);
    }

    @Test
    @Transactional
    void putNonExistingProcesVerbal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        procesVerbal.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcesVerbalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, procesVerbal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(procesVerbal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcesVerbal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        procesVerbal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcesVerbalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(procesVerbal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcesVerbal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        procesVerbal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcesVerbalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(procesVerbal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcesVerbalWithPatch() throws Exception {
        // Initialize the database
        procesVerbalRepository.saveAndFlush(procesVerbal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the procesVerbal using partial update
        ProcesVerbal partialUpdatedProcesVerbal = new ProcesVerbal();
        partialUpdatedProcesVerbal.setId(procesVerbal.getId());

        partialUpdatedProcesVerbal.resum(UPDATED_RESUM);

        restProcesVerbalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesVerbal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcesVerbal))
            )
            .andExpect(status().isOk());

        // Validate the ProcesVerbal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcesVerbalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProcesVerbal, procesVerbal),
            getPersistedProcesVerbal(procesVerbal)
        );
    }

    @Test
    @Transactional
    void fullUpdateProcesVerbalWithPatch() throws Exception {
        // Initialize the database
        procesVerbalRepository.saveAndFlush(procesVerbal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the procesVerbal using partial update
        ProcesVerbal partialUpdatedProcesVerbal = new ProcesVerbal();
        partialUpdatedProcesVerbal.setId(procesVerbal.getId());

        partialUpdatedProcesVerbal.numProcesV(UPDATED_NUM_PROCES_V).resum(UPDATED_RESUM);

        restProcesVerbalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcesVerbal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProcesVerbal))
            )
            .andExpect(status().isOk());

        // Validate the ProcesVerbal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProcesVerbalUpdatableFieldsEquals(partialUpdatedProcesVerbal, getPersistedProcesVerbal(partialUpdatedProcesVerbal));
    }

    @Test
    @Transactional
    void patchNonExistingProcesVerbal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        procesVerbal.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcesVerbalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, procesVerbal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(procesVerbal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcesVerbal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        procesVerbal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcesVerbalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(procesVerbal))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcesVerbal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        procesVerbal.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcesVerbalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(procesVerbal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcesVerbal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcesVerbal() throws Exception {
        // Initialize the database
        procesVerbalRepository.saveAndFlush(procesVerbal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the procesVerbal
        restProcesVerbalMockMvc
            .perform(delete(ENTITY_API_URL_ID, procesVerbal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return procesVerbalRepository.count();
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

    protected ProcesVerbal getPersistedProcesVerbal(ProcesVerbal procesVerbal) {
        return procesVerbalRepository.findById(procesVerbal.getId()).orElseThrow();
    }

    protected void assertPersistedProcesVerbalToMatchAllProperties(ProcesVerbal expectedProcesVerbal) {
        assertProcesVerbalAllPropertiesEquals(expectedProcesVerbal, getPersistedProcesVerbal(expectedProcesVerbal));
    }

    protected void assertPersistedProcesVerbalToMatchUpdatableProperties(ProcesVerbal expectedProcesVerbal) {
        assertProcesVerbalAllUpdatablePropertiesEquals(expectedProcesVerbal, getPersistedProcesVerbal(expectedProcesVerbal));
    }
}
