package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DepartementAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Departement;
import com.mycompany.myapp.repository.DepartementRepository;
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
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepartementResourceIT {

    private static final Integer DEFAULT_NUM = 1;
    private static final Integer UPDATED_NUM = 2;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartementMockMvc;

    private Departement departement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement().num(DEFAULT_NUM).nom(DEFAULT_NOM);
        return departement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity(EntityManager em) {
        Departement departement = new Departement().num(UPDATED_NUM).nom(UPDATED_NOM);
        return departement;
    }

    @BeforeEach
    public void initTest() {
        departement = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Departement
        var returnedDepartement = om.readValue(
            restDepartementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Departement.class
        );

        // Validate the Departement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDepartementUpdatableFieldsEquals(returnedDepartement, getPersistedDepartement(returnedDepartement));
    }

    @Test
    @Transactional
    void createDepartementWithExistingId() throws Exception {
        // Create the Departement with an existing ID
        departement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departement)))
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartements() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].num").value(hasItem(DEFAULT_NUM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL_ID, departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.num").value(DEFAULT_NUM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement.num(UPDATED_NUM).nom(UPDATED_NOM);

        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepartementToMatchAllProperties(updatedDepartement);
    }

    @Test
    @Transactional
    void putNonExistingDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(departement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.num(UPDATED_NUM);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepartement, departement),
            getPersistedDepartement(departement)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.num(UPDATED_NUM).nom(UPDATED_NOM);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepartementUpdatableFieldsEquals(partialUpdatedDepartement, getPersistedDepartement(partialUpdatedDepartement));
    }

    @Test
    @Transactional
    void patchNonExistingDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        departement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(departement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the departement
        restDepartementMockMvc
            .perform(delete(ENTITY_API_URL_ID, departement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return departementRepository.count();
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

    protected Departement getPersistedDepartement(Departement departement) {
        return departementRepository.findById(departement.getId()).orElseThrow();
    }

    protected void assertPersistedDepartementToMatchAllProperties(Departement expectedDepartement) {
        assertDepartementAllPropertiesEquals(expectedDepartement, getPersistedDepartement(expectedDepartement));
    }

    protected void assertPersistedDepartementToMatchUpdatableProperties(Departement expectedDepartement) {
        assertDepartementAllUpdatablePropertiesEquals(expectedDepartement, getPersistedDepartement(expectedDepartement));
    }
}
