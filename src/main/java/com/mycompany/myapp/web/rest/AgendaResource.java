package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Agenda;
import com.mycompany.myapp.repository.AgendaRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Agenda}.
 */
@RestController
@RequestMapping("/api/agenda")
@Transactional
public class AgendaResource {

    private final Logger log = LoggerFactory.getLogger(AgendaResource.class);

    private static final String ENTITY_NAME = "agenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaRepository agendaRepository;

    public AgendaResource(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    /**
     * {@code POST  /agenda} : Create a new agenda.
     *
     * @param agenda the agenda to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agenda, or with status {@code 400 (Bad Request)} if the agenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Agenda> createAgenda(@RequestBody Agenda agenda) throws URISyntaxException {
        log.debug("REST request to save Agenda : {}", agenda);
        if (agenda.getId() != null) {
            throw new BadRequestAlertException("A new agenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agenda = agendaRepository.save(agenda);
        return ResponseEntity.created(new URI("/api/agenda/" + agenda.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, agenda.getId().toString()))
            .body(agenda);
    }

    /**
     * {@code PUT  /agenda/:id} : Updates an existing agenda.
     *
     * @param id the id of the agenda to save.
     * @param agenda the agenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenda,
     * or with status {@code 400 (Bad Request)} if the agenda is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Agenda> updateAgenda(@PathVariable(value = "id", required = false) final Long id, @RequestBody Agenda agenda)
        throws URISyntaxException {
        log.debug("REST request to update Agenda : {}, {}", id, agenda);
        if (agenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agenda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agenda = agendaRepository.save(agenda);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agenda.getId().toString()))
            .body(agenda);
    }

    /**
     * {@code PATCH  /agenda/:id} : Partial updates given fields of an existing agenda, field will ignore if it is null
     *
     * @param id the id of the agenda to save.
     * @param agenda the agenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenda,
     * or with status {@code 400 (Bad Request)} if the agenda is not valid,
     * or with status {@code 404 (Not Found)} if the agenda is not found,
     * or with status {@code 500 (Internal Server Error)} if the agenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Agenda> partialUpdateAgenda(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Agenda agenda
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agenda partially : {}, {}", id, agenda);
        if (agenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agenda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Agenda> result = agendaRepository
            .findById(agenda.getId())
            .map(existingAgenda -> {
                if (agenda.getNumAgenda() != null) {
                    existingAgenda.setNumAgenda(agenda.getNumAgenda());
                }
                if (agenda.getDateCreation() != null) {
                    existingAgenda.setDateCreation(agenda.getDateCreation());
                }

                return existingAgenda;
            })
            .map(agendaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agenda.getId().toString())
        );
    }

    /**
     * {@code GET  /agenda} : get all the agenda.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agenda in body.
     */
    @GetMapping("")
    public List<Agenda> getAllAgenda(@RequestParam(name = "filter", required = false) String filter) {
        if ("employe-is-null".equals(filter)) {
            log.debug("REST request to get all Agendas where employe is null");
            return StreamSupport.stream(agendaRepository.findAll().spliterator(), false)
                .filter(agenda -> agenda.getEmploye() == null)
                .toList();
        }
        log.debug("REST request to get all Agenda");
        return agendaRepository.findAll();
    }

    /**
     * {@code GET  /agenda/:id} : get the "id" agenda.
     *
     * @param id the id of the agenda to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agenda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Agenda> getAgenda(@PathVariable("id") Long id) {
        log.debug("REST request to get Agenda : {}", id);
        Optional<Agenda> agenda = agendaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agenda);
    }

    /**
     * {@code DELETE  /agenda/:id} : delete the "id" agenda.
     *
     * @param id the id of the agenda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable("id") Long id) {
        log.debug("REST request to delete Agenda : {}", id);
        agendaRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
