package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Departement;
import com.mycompany.myapp.repository.DepartementRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Departement}.
 */
@RestController
@RequestMapping("/api/departements")
@Transactional
public class DepartementResource {

    private final Logger log = LoggerFactory.getLogger(DepartementResource.class);

    private static final String ENTITY_NAME = "departement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartementRepository departementRepository;

    public DepartementResource(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    /**
     * {@code POST  /departements} : Create a new departement.
     *
     * @param departement the departement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departement, or with status {@code 400 (Bad Request)} if the departement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Departement> createDepartement(@RequestBody Departement departement) throws URISyntaxException {
        log.debug("REST request to save Departement : {}", departement);
        if (departement.getId() != null) {
            throw new BadRequestAlertException("A new departement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        departement = departementRepository.save(departement);
        return ResponseEntity.created(new URI("/api/departements/" + departement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, departement.getId().toString()))
            .body(departement);
    }

    /**
     * {@code PUT  /departements/:id} : Updates an existing departement.
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Departement> updateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to update Departement : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        departement = departementRepository.save(departement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departement.getId().toString()))
            .body(departement);
    }

    /**
     * {@code PATCH  /departements/:id} : Partial updates given fields of an existing departement, field will ignore if it is null
     *
     * @param id the id of the departement to save.
     * @param departement the departement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departement,
     * or with status {@code 400 (Bad Request)} if the departement is not valid,
     * or with status {@code 404 (Not Found)} if the departement is not found,
     * or with status {@code 500 (Internal Server Error)} if the departement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Departement> partialUpdateDepartement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Departement departement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Departement partially : {}, {}", id, departement);
        if (departement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Departement> result = departementRepository
            .findById(departement.getId())
            .map(existingDepartement -> {
                if (departement.getNum() != null) {
                    existingDepartement.setNum(departement.getNum());
                }
                if (departement.getNom() != null) {
                    existingDepartement.setNom(departement.getNom());
                }

                return existingDepartement;
            })
            .map(departementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departement.getId().toString())
        );
    }

    /**
     * {@code GET  /departements} : get all the departements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departements in body.
     */
    @GetMapping("")
    public List<Departement> getAllDepartements() {
        log.debug("REST request to get all Departements");
        return departementRepository.findAll();
    }

    /**
     * {@code GET  /departements/:id} : get the "id" departement.
     *
     * @param id the id of the departement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartement(@PathVariable("id") Long id) {
        log.debug("REST request to get Departement : {}", id);
        Optional<Departement> departement = departementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(departement);
    }

    /**
     * {@code DELETE  /departements/:id} : delete the "id" departement.
     *
     * @param id the id of the departement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable("id") Long id) {
        log.debug("REST request to delete Departement : {}", id);
        departementRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
