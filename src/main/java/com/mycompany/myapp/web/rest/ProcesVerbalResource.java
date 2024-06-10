package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ProcesVerbal;
import com.mycompany.myapp.repository.ProcesVerbalRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProcesVerbal}.
 */
@RestController
@RequestMapping("/api/proces-verbals")
@Transactional
public class ProcesVerbalResource {

    private final Logger log = LoggerFactory.getLogger(ProcesVerbalResource.class);

    private static final String ENTITY_NAME = "procesVerbal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcesVerbalRepository procesVerbalRepository;

    public ProcesVerbalResource(ProcesVerbalRepository procesVerbalRepository) {
        this.procesVerbalRepository = procesVerbalRepository;
    }

    /**
     * {@code POST  /proces-verbals} : Create a new procesVerbal.
     *
     * @param procesVerbal the procesVerbal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new procesVerbal, or with status {@code 400 (Bad Request)} if the procesVerbal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcesVerbal> createProcesVerbal(@RequestBody ProcesVerbal procesVerbal) throws URISyntaxException {
        log.debug("REST request to save ProcesVerbal : {}", procesVerbal);
        if (procesVerbal.getId() != null) {
            throw new BadRequestAlertException("A new procesVerbal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        procesVerbal = procesVerbalRepository.save(procesVerbal);
        return ResponseEntity.created(new URI("/api/proces-verbals/" + procesVerbal.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, procesVerbal.getId().toString()))
            .body(procesVerbal);
    }

    /**
     * {@code PUT  /proces-verbals/:id} : Updates an existing procesVerbal.
     *
     * @param id the id of the procesVerbal to save.
     * @param procesVerbal the procesVerbal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procesVerbal,
     * or with status {@code 400 (Bad Request)} if the procesVerbal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the procesVerbal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcesVerbal> updateProcesVerbal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcesVerbal procesVerbal
    ) throws URISyntaxException {
        log.debug("REST request to update ProcesVerbal : {}, {}", id, procesVerbal);
        if (procesVerbal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, procesVerbal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!procesVerbalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        procesVerbal = procesVerbalRepository.save(procesVerbal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, procesVerbal.getId().toString()))
            .body(procesVerbal);
    }

    /**
     * {@code PATCH  /proces-verbals/:id} : Partial updates given fields of an existing procesVerbal, field will ignore if it is null
     *
     * @param id the id of the procesVerbal to save.
     * @param procesVerbal the procesVerbal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procesVerbal,
     * or with status {@code 400 (Bad Request)} if the procesVerbal is not valid,
     * or with status {@code 404 (Not Found)} if the procesVerbal is not found,
     * or with status {@code 500 (Internal Server Error)} if the procesVerbal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcesVerbal> partialUpdateProcesVerbal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProcesVerbal procesVerbal
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcesVerbal partially : {}, {}", id, procesVerbal);
        if (procesVerbal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, procesVerbal.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!procesVerbalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcesVerbal> result = procesVerbalRepository
            .findById(procesVerbal.getId())
            .map(existingProcesVerbal -> {
                if (procesVerbal.getNumProcesV() != null) {
                    existingProcesVerbal.setNumProcesV(procesVerbal.getNumProcesV());
                }
                if (procesVerbal.getResum() != null) {
                    existingProcesVerbal.setResum(procesVerbal.getResum());
                }

                return existingProcesVerbal;
            })
            .map(procesVerbalRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, procesVerbal.getId().toString())
        );
    }

    /**
     * {@code GET  /proces-verbals} : get all the procesVerbals.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procesVerbals in body.
     */
    @GetMapping("")
    public List<ProcesVerbal> getAllProcesVerbals(@RequestParam(name = "filter", required = false) String filter) {
        if ("activitedept-is-null".equals(filter)) {
            log.debug("REST request to get all ProcesVerbals where activiteDept is null");
            return StreamSupport.stream(procesVerbalRepository.findAll().spliterator(), false)
                .filter(procesVerbal -> procesVerbal.getActiviteDept() == null)
                .toList();
        }
        log.debug("REST request to get all ProcesVerbals");
        return procesVerbalRepository.findAll();
    }

    /**
     * {@code GET  /proces-verbals/:id} : get the "id" procesVerbal.
     *
     * @param id the id of the procesVerbal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procesVerbal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcesVerbal> getProcesVerbal(@PathVariable("id") Long id) {
        log.debug("REST request to get ProcesVerbal : {}", id);
        Optional<ProcesVerbal> procesVerbal = procesVerbalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(procesVerbal);
    }

    /**
     * {@code DELETE  /proces-verbals/:id} : delete the "id" procesVerbal.
     *
     * @param id the id of the procesVerbal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcesVerbal(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProcesVerbal : {}", id);
        procesVerbalRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
