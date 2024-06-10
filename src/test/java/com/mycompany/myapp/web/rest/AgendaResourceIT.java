package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AgendaAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Agenda;
import com.mycompany.myapp.repository.AgendaRepository;
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
 * Integration tests for the {@link AgendaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgendaResourceIT {

    private static final Integer DEFAULT_NUM_AGENDA = 1;
    private static final Integer UPDATED_NUM_AGENDA = 2;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/agenda";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgendaMockMvc;

    private Agenda agenda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createEntity(EntityManager em) {
        Agenda agenda = new Agenda().numAgenda(DEFAULT_NUM_AGENDA).dateCreation(DEFAULT_DATE_CREATION);
        return agenda;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createUpdatedEntity(EntityManager em) {
        Agenda agenda = new Agenda().numAgenda(UPDATED_NUM_AGENDA).dateCreation(UPDATED_DATE_CREATION);
        return agenda;
    }

    @BeforeEach
    public void initTest() {
        agenda = createEntity(em);
    }

    @Test
    @Transactional
    void createAgenda() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Agenda
        var returnedAgenda = om.readValue(
            restAgendaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agenda)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Agenda.class
        );

        // Validate the Agenda in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgendaUpdatableFieldsEquals(returnedAgenda, getPersistedAgenda(returnedAgenda));
    }

    @Test
    @Transactional
    void createAgendaWithExistingId() throws Exception {
        // Create the Agenda with an existing ID
        agenda.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agenda)))
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].numAgenda").value(hasItem(DEFAULT_NUM_AGENDA)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get the agenda
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL_ID, agenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agenda.getId().intValue()))
            .andExpect(jsonPath("$.numAgenda").value(DEFAULT_NUM_AGENDA))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAgenda() throws Exception {
        // Get the agenda
        restAgendaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agenda
        Agenda updatedAgenda = agendaRepository.findById(agenda.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgenda are not directly saved in db
        em.detach(updatedAgenda);
        updatedAgenda.numAgenda(UPDATED_NUM_AGENDA).dateCreation(UPDATED_DATE_CREATION);

        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgenda.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgendaToMatchAllProperties(updatedAgenda);
    }

    @Test
    @Transactional
    void putNonExistingAgenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agenda.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(put(ENTITY_API_URL_ID, agenda.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agenda)))
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agenda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agenda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agenda)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgendaWithPatch() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agenda using partial update
        Agenda partialUpdatedAgenda = new Agenda();
        partialUpdatedAgenda.setId(agenda.getId());

        partialUpdatedAgenda.dateCreation(UPDATED_DATE_CREATION);

        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgendaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAgenda, agenda), getPersistedAgenda(agenda));
    }

    @Test
    @Transactional
    void fullUpdateAgendaWithPatch() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agenda using partial update
        Agenda partialUpdatedAgenda = new Agenda();
        partialUpdatedAgenda.setId(agenda.getId());

        partialUpdatedAgenda.numAgenda(UPDATED_NUM_AGENDA).dateCreation(UPDATED_DATE_CREATION);

        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgenda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgendaUpdatableFieldsEquals(partialUpdatedAgenda, getPersistedAgenda(partialUpdatedAgenda));
    }

    @Test
    @Transactional
    void patchNonExistingAgenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agenda.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agenda.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agenda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgenda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agenda.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agenda)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agenda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agenda
        restAgendaMockMvc
            .perform(delete(ENTITY_API_URL_ID, agenda.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agendaRepository.count();
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

    protected Agenda getPersistedAgenda(Agenda agenda) {
        return agendaRepository.findById(agenda.getId()).orElseThrow();
    }

    protected void assertPersistedAgendaToMatchAllProperties(Agenda expectedAgenda) {
        assertAgendaAllPropertiesEquals(expectedAgenda, getPersistedAgenda(expectedAgenda));
    }

    protected void assertPersistedAgendaToMatchUpdatableProperties(Agenda expectedAgenda) {
        assertAgendaAllUpdatablePropertiesEquals(expectedAgenda, getPersistedAgenda(expectedAgenda));
    }
}
