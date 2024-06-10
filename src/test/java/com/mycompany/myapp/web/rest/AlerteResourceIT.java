package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AlerteAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Alerte;
import com.mycompany.myapp.repository.AlerteRepository;
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
 * Integration tests for the {@link AlerteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlerteResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELAIS = 1;
    private static final Integer UPDATED_DELAIS = 2;

    private static final String ENTITY_API_URL = "/api/alertes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlerteMockMvc;

    private Alerte alerte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createEntity(EntityManager em) {
        Alerte alerte = new Alerte().type(DEFAULT_TYPE).delais(DEFAULT_DELAIS);
        return alerte;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createUpdatedEntity(EntityManager em) {
        Alerte alerte = new Alerte().type(UPDATED_TYPE).delais(UPDATED_DELAIS);
        return alerte;
    }

    @BeforeEach
    public void initTest() {
        alerte = createEntity(em);
    }

    @Test
    @Transactional
    void createAlerte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alerte
        var returnedAlerte = om.readValue(
            restAlerteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerte)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Alerte.class
        );

        // Validate the Alerte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlerteUpdatableFieldsEquals(returnedAlerte, getPersistedAlerte(returnedAlerte));
    }

    @Test
    @Transactional
    void createAlerteWithExistingId() throws Exception {
        // Create the Alerte with an existing ID
        alerte.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerte)))
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlertes() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerte.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].delais").value(hasItem(DEFAULT_DELAIS)));
    }

    @Test
    @Transactional
    void getAlerte() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        // Get the alerte
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL_ID, alerte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerte.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.delais").value(DEFAULT_DELAIS));
    }

    @Test
    @Transactional
    void getNonExistingAlerte() throws Exception {
        // Get the alerte
        restAlerteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlerte() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerte
        Alerte updatedAlerte = alerteRepository.findById(alerte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlerte are not directly saved in db
        em.detach(updatedAlerte);
        updatedAlerte.type(UPDATED_TYPE).delais(UPDATED_DELAIS);

        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlerte.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlerteToMatchAllProperties(updatedAlerte);
    }

    @Test
    @Transactional
    void putNonExistingAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(put(ENTITY_API_URL_ID, alerte.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerte)))
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte.type(UPDATED_TYPE).delais(UPDATED_DELAIS);

        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlerte, alerte), getPersistedAlerte(alerte));
    }

    @Test
    @Transactional
    void fullUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte.type(UPDATED_TYPE).delais(UPDATED_DELAIS);

        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteUpdatableFieldsEquals(partialUpdatedAlerte, getPersistedAlerte(partialUpdatedAlerte));
    }

    @Test
    @Transactional
    void patchNonExistingAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alerte.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerte))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alerte)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlerte() throws Exception {
        // Initialize the database
        alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alerte
        restAlerteMockMvc
            .perform(delete(ENTITY_API_URL_ID, alerte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alerteRepository.count();
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

    protected Alerte getPersistedAlerte(Alerte alerte) {
        return alerteRepository.findById(alerte.getId()).orElseThrow();
    }

    protected void assertPersistedAlerteToMatchAllProperties(Alerte expectedAlerte) {
        assertAlerteAllPropertiesEquals(expectedAlerte, getPersistedAlerte(expectedAlerte));
    }

    protected void assertPersistedAlerteToMatchUpdatableProperties(Alerte expectedAlerte) {
        assertAlerteAllUpdatablePropertiesEquals(expectedAlerte, getPersistedAlerte(expectedAlerte));
    }
}
