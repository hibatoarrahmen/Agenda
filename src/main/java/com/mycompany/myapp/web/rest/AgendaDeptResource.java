package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AgendaDept;
import com.mycompany.myapp.repository.AgendaDeptRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AgendaDept}.
 */
@RestController
@RequestMapping("/api/agenda-depts")
@Transactional
public class AgendaDeptResource {

    private final Logger log = LoggerFactory.getLogger(AgendaDeptResource.class);

    private static final String ENTITY_NAME = "agendaDept";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaDeptRepository agendaDeptRepository;

    public AgendaDeptResource(AgendaDeptRepository agendaDeptRepository) {
        this.agendaDeptRepository = agendaDeptRepository;
    }

    /**
     * {@code POST  /agenda-depts} : Create a new agendaDept.
     *
     * @param agendaDept the agendaDept to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agendaDept, or with status {@code 400 (Bad Request)} if the agendaDept has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgendaDept> createAgendaDept(@RequestBody AgendaDept agendaDept) throws URISyntaxException {
        log.debug("REST request to save AgendaDept : {}", agendaDept);
        if (agendaDept.getId() != null) {
            throw new BadRequestAlertException("A new agendaDept cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agendaDept = agendaDeptRepository.save(agendaDept);
        return ResponseEntity.created(new URI("/api/agenda-depts/" + agendaDept.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, agendaDept.getId().toString()))
            .body(agendaDept);
    }

    /**
     * {@code PUT  /agenda-depts/:id} : Updates an existing agendaDept.
     *
     * @param id the id of the agendaDept to save.
     * @param agendaDept the agendaDept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaDept,
     * or with status {@code 400 (Bad Request)} if the agendaDept is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agendaDept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgendaDept> updateAgendaDept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaDept agendaDept
    ) throws URISyntaxException {
        log.debug("REST request to update AgendaDept : {}, {}", id, agendaDept);
        if (agendaDept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaDept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaDeptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agendaDept = agendaDeptRepository.save(agendaDept);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agendaDept.getId().toString()))
            .body(agendaDept);
    }

    /**
     * {@code PATCH  /agenda-depts/:id} : Partial updates given fields of an existing agendaDept, field will ignore if it is null
     *
     * @param id the id of the agendaDept to save.
     * @param agendaDept the agendaDept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agendaDept,
     * or with status {@code 400 (Bad Request)} if the agendaDept is not valid,
     * or with status {@code 404 (Not Found)} if the agendaDept is not found,
     * or with status {@code 500 (Internal Server Error)} if the agendaDept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgendaDept> partialUpdateAgendaDept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AgendaDept agendaDept
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgendaDept partially : {}, {}", id, agendaDept);
        if (agendaDept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agendaDept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaDeptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgendaDept> result = agendaDeptRepository
            .findById(agendaDept.getId())
            .map(existingAgendaDept -> {
                if (agendaDept.getNumAgenda() != null) {
                    existingAgendaDept.setNumAgenda(agendaDept.getNumAgenda());
                }
                if (agendaDept.getDateMAJ() != null) {
                    existingAgendaDept.setDateMAJ(agendaDept.getDateMAJ());
                }

                return existingAgendaDept;
            })
            .map(agendaDeptRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, agendaDept.getId().toString())
        );
    }

    /**
     * {@code GET  /agenda-depts} : get all the agendaDepts.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agendaDepts in body.
     */
    @GetMapping("")
    public List<AgendaDept> getAllAgendaDepts(@RequestParam(name = "filter", required = false) String filter) {
        if ("departement-is-null".equals(filter)) {
            log.debug("REST request to get all AgendaDepts where departement is null");
            return StreamSupport.stream(agendaDeptRepository.findAll().spliterator(), false)
                .filter(agendaDept -> agendaDept.getDepartement() == null)
                .toList();
        }
        log.debug("REST request to get all AgendaDepts");
        return agendaDeptRepository.findAll();
    }

    /**
     * {@code GET  /agenda-depts/:id} : get the "id" agendaDept.
     *
     * @param id the id of the agendaDept to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agendaDept, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgendaDept> getAgendaDept(@PathVariable("id") Long id) {
        log.debug("REST request to get AgendaDept : {}", id);
        Optional<AgendaDept> agendaDept = agendaDeptRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(agendaDept);
    }

    /**
     * {@code DELETE  /agenda-depts/:id} : delete the "id" agendaDept.
     *
     * @param id the id of the agendaDept to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendaDept(@PathVariable("id") Long id) {
        log.debug("REST request to delete AgendaDept : {}", id);
        agendaDeptRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
