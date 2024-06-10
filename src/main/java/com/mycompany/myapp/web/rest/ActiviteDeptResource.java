package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ActiviteDept;
import com.mycompany.myapp.repository.ActiviteDeptRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ActiviteDept}.
 */
@RestController
@RequestMapping("/api/activite-depts")
@Transactional
public class ActiviteDeptResource {

    private final Logger log = LoggerFactory.getLogger(ActiviteDeptResource.class);

    private static final String ENTITY_NAME = "activiteDept";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActiviteDeptRepository activiteDeptRepository;

    public ActiviteDeptResource(ActiviteDeptRepository activiteDeptRepository) {
        this.activiteDeptRepository = activiteDeptRepository;
    }

    /**
     * {@code POST  /activite-depts} : Create a new activiteDept.
     *
     * @param activiteDept the activiteDept to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activiteDept, or with status {@code 400 (Bad Request)} if the activiteDept has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ActiviteDept> createActiviteDept(@RequestBody ActiviteDept activiteDept) throws URISyntaxException {
        log.debug("REST request to save ActiviteDept : {}", activiteDept);
        if (activiteDept.getId() != null) {
            throw new BadRequestAlertException("A new activiteDept cannot already have an ID", ENTITY_NAME, "idexists");
        }
        activiteDept = activiteDeptRepository.save(activiteDept);
        return ResponseEntity.created(new URI("/api/activite-depts/" + activiteDept.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, activiteDept.getId().toString()))
            .body(activiteDept);
    }

    /**
     * {@code PUT  /activite-depts/:id} : Updates an existing activiteDept.
     *
     * @param id the id of the activiteDept to save.
     * @param activiteDept the activiteDept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activiteDept,
     * or with status {@code 400 (Bad Request)} if the activiteDept is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activiteDept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActiviteDept> updateActiviteDept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActiviteDept activiteDept
    ) throws URISyntaxException {
        log.debug("REST request to update ActiviteDept : {}, {}", id, activiteDept);
        if (activiteDept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activiteDept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activiteDeptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        activiteDept = activiteDeptRepository.save(activiteDept);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activiteDept.getId().toString()))
            .body(activiteDept);
    }

    /**
     * {@code PATCH  /activite-depts/:id} : Partial updates given fields of an existing activiteDept, field will ignore if it is null
     *
     * @param id the id of the activiteDept to save.
     * @param activiteDept the activiteDept to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activiteDept,
     * or with status {@code 400 (Bad Request)} if the activiteDept is not valid,
     * or with status {@code 404 (Not Found)} if the activiteDept is not found,
     * or with status {@code 500 (Internal Server Error)} if the activiteDept couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActiviteDept> partialUpdateActiviteDept(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActiviteDept activiteDept
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActiviteDept partially : {}, {}", id, activiteDept);
        if (activiteDept.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activiteDept.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activiteDeptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActiviteDept> result = activiteDeptRepository
            .findById(activiteDept.getId())
            .map(existingActiviteDept -> {
                if (activiteDept.getNumAct() != null) {
                    existingActiviteDept.setNumAct(activiteDept.getNumAct());
                }
                if (activiteDept.getTypeD() != null) {
                    existingActiviteDept.setTypeD(activiteDept.getTypeD());
                }
                if (activiteDept.getDescript() != null) {
                    existingActiviteDept.setDescript(activiteDept.getDescript());
                }
                if (activiteDept.getDateAct() != null) {
                    existingActiviteDept.setDateAct(activiteDept.getDateAct());
                }
                if (activiteDept.gethDebut() != null) {
                    existingActiviteDept.sethDebut(activiteDept.gethDebut());
                }
                if (activiteDept.gethFin() != null) {
                    existingActiviteDept.sethFin(activiteDept.gethFin());
                }
                if (activiteDept.getDateCreation() != null) {
                    existingActiviteDept.setDateCreation(activiteDept.getDateCreation());
                }
                if (activiteDept.getCreateur() != null) {
                    existingActiviteDept.setCreateur(activiteDept.getCreateur());
                }

                return existingActiviteDept;
            })
            .map(activiteDeptRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, activiteDept.getId().toString())
        );
    }

    /**
     * {@code GET  /activite-depts} : get all the activiteDepts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activiteDepts in body.
     */
    @GetMapping("")
    public List<ActiviteDept> getAllActiviteDepts() {
        log.debug("REST request to get all ActiviteDepts");
        return activiteDeptRepository.findAll();
    }

    /**
     * {@code GET  /activite-depts/:id} : get the "id" activiteDept.
     *
     * @param id the id of the activiteDept to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activiteDept, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActiviteDept> getActiviteDept(@PathVariable("id") Long id) {
        log.debug("REST request to get ActiviteDept : {}", id);
        Optional<ActiviteDept> activiteDept = activiteDeptRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activiteDept);
    }

    /**
     * {@code DELETE  /activite-depts/:id} : delete the "id" activiteDept.
     *
     * @param id the id of the activiteDept to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActiviteDept(@PathVariable("id") Long id) {
        log.debug("REST request to delete ActiviteDept : {}", id);
        activiteDeptRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
