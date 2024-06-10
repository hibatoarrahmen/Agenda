package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EmployeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employe;
import com.mycompany.myapp.repository.EmployeRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeResourceIT {

    private static final Integer DEFAULT_NUM_EMPLOYE = 1;
    private static final Integer UPDATED_NUM_EMPLOYE = 2;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_INTERN = "AAAAAAAAAA";
    private static final String UPDATED_TEL_INTERN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_NIVEAU = 1;
    private static final Integer UPDATED_NIVEAU = 2;

    private static final String ENTITY_API_URL = "/api/employes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeRepository employeRepository;

    @Mock
    private EmployeRepository employeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeMockMvc;

    private Employe employe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employe createEntity(EntityManager em) {
        Employe employe = new Employe()
            .numEmploye(DEFAULT_NUM_EMPLOYE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .telIntern(DEFAULT_TEL_INTERN)
            .email(DEFAULT_EMAIL)
            .niveau(DEFAULT_NIVEAU);
        return employe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employe createUpdatedEntity(EntityManager em) {
        Employe employe = new Employe()
            .numEmploye(UPDATED_NUM_EMPLOYE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telIntern(UPDATED_TEL_INTERN)
            .email(UPDATED_EMAIL)
            .niveau(UPDATED_NIVEAU);
        return employe;
    }

    @BeforeEach
    public void initTest() {
        employe = createEntity(em);
    }

    @Test
    @Transactional
    void createEmploye() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Employe
        var returnedEmploye = om.readValue(
            restEmployeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Employe.class
        );

        // Validate the Employe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeUpdatableFieldsEquals(returnedEmploye, getPersistedEmploye(returnedEmploye));
    }

    @Test
    @Transactional
    void createEmployeWithExistingId() throws Exception {
        // Create the Employe with an existing ID
        employe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employe)))
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployes() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get all the employeList
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employe.getId().intValue())))
            .andExpect(jsonPath("$.[*].numEmploye").value(hasItem(DEFAULT_NUM_EMPLOYE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telIntern").value(hasItem(DEFAULT_TEL_INTERN)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(employeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        // Get the employe
        restEmployeMockMvc
            .perform(get(ENTITY_API_URL_ID, employe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employe.getId().intValue()))
            .andExpect(jsonPath("$.numEmploye").value(DEFAULT_NUM_EMPLOYE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.telIntern").value(DEFAULT_TEL_INTERN))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU));
    }

    @Test
    @Transactional
    void getNonExistingEmploye() throws Exception {
        // Get the employe
        restEmployeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employe
        Employe updatedEmploye = employeRepository.findById(employe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmploye are not directly saved in db
        em.detach(updatedEmploye);
        updatedEmploye
            .numEmploye(UPDATED_NUM_EMPLOYE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telIntern(UPDATED_TEL_INTERN)
            .email(UPDATED_EMAIL)
            .niveau(UPDATED_NIVEAU);

        restEmployeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmploye.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmploye))
            )
            .andExpect(status().isOk());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeToMatchAllProperties(updatedEmploye);
    }

    @Test
    @Transactional
    void putNonExistingEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(put(ENTITY_API_URL_ID, employe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employe)))
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeWithPatch() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employe using partial update
        Employe partialUpdatedEmploye = new Employe();
        partialUpdatedEmploye.setId(employe.getId());

        partialUpdatedEmploye
            .numEmploye(UPDATED_NUM_EMPLOYE)
            .nom(UPDATED_NOM)
            .telIntern(UPDATED_TEL_INTERN)
            .email(UPDATED_EMAIL)
            .niveau(UPDATED_NIVEAU);

        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploye.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmploye))
            )
            .andExpect(status().isOk());

        // Validate the Employe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmploye, employe), getPersistedEmploye(employe));
    }

    @Test
    @Transactional
    void fullUpdateEmployeWithPatch() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employe using partial update
        Employe partialUpdatedEmploye = new Employe();
        partialUpdatedEmploye.setId(employe.getId());

        partialUpdatedEmploye
            .numEmploye(UPDATED_NUM_EMPLOYE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telIntern(UPDATED_TEL_INTERN)
            .email(UPDATED_EMAIL)
            .niveau(UPDATED_NIVEAU);

        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmploye.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmploye))
            )
            .andExpect(status().isOk());

        // Validate the Employe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeUpdatableFieldsEquals(partialUpdatedEmploye, getPersistedEmploye(partialUpdatedEmploye));
    }

    @Test
    @Transactional
    void patchNonExistingEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employe.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmploye() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmploye() throws Exception {
        // Initialize the database
        employeRepository.saveAndFlush(employe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employe
        restEmployeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeRepository.count();
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

    protected Employe getPersistedEmploye(Employe employe) {
        return employeRepository.findById(employe.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeToMatchAllProperties(Employe expectedEmploye) {
        assertEmployeAllPropertiesEquals(expectedEmploye, getPersistedEmploye(expectedEmploye));
    }

    protected void assertPersistedEmployeToMatchUpdatableProperties(Employe expectedEmploye) {
        assertEmployeAllUpdatablePropertiesEquals(expectedEmploye, getPersistedEmploye(expectedEmploye));
    }
}
