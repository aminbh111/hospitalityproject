package tn.greencode.hospitality.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.greencode.hospitality.domain.SpaData;
import tn.greencode.hospitality.repository.SpaDataRepository;
import tn.greencode.hospitality.service.SpaDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.SpaData}.
 */
@RestController
@RequestMapping("/api")
public class SpaDataResource {

    private final Logger log = LoggerFactory.getLogger(SpaDataResource.class);

    private static final String ENTITY_NAME = "spaData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaDataService spaDataService;

    private final SpaDataRepository spaDataRepository;

    public SpaDataResource(SpaDataService spaDataService, SpaDataRepository spaDataRepository) {
        this.spaDataService = spaDataService;
        this.spaDataRepository = spaDataRepository;
    }

    /**
     * {@code POST  /spa-data} : Create a new spaData.
     *
     * @param spaData the spaData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spaData, or with status {@code 400 (Bad Request)} if the spaData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spa-data")
    public ResponseEntity<SpaData> createSpaData(@RequestBody SpaData spaData) throws URISyntaxException {
        log.debug("REST request to save SpaData : {}", spaData);
        if (spaData.getId() != null) {
            throw new BadRequestAlertException("A new spaData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpaData result = spaDataService.save(spaData);
        return ResponseEntity
            .created(new URI("/api/spa-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spa-data/:id} : Updates an existing spaData.
     *
     * @param id the id of the spaData to save.
     * @param spaData the spaData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaData,
     * or with status {@code 400 (Bad Request)} if the spaData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spaData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spa-data/{id}")
    public ResponseEntity<SpaData> updateSpaData(@PathVariable(value = "id", required = false) final Long id, @RequestBody SpaData spaData)
        throws URISyntaxException {
        log.debug("REST request to update SpaData : {}, {}", id, spaData);
        if (spaData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spaData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpaData result = spaDataService.update(spaData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spaData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spa-data/:id} : Partial updates given fields of an existing spaData, field will ignore if it is null
     *
     * @param id the id of the spaData to save.
     * @param spaData the spaData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spaData,
     * or with status {@code 400 (Bad Request)} if the spaData is not valid,
     * or with status {@code 404 (Not Found)} if the spaData is not found,
     * or with status {@code 500 (Internal Server Error)} if the spaData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spa-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpaData> partialUpdateSpaData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SpaData spaData
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpaData partially : {}, {}", id, spaData);
        if (spaData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spaData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpaData> result = spaDataService.partialUpdate(spaData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spaData.getId().toString())
        );
    }

    /**
     * {@code GET  /spa-data} : get all the spaData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spaData in body.
     */
    @GetMapping("/spa-data")
    public List<SpaData> getAllSpaData() {
        log.debug("REST request to get all SpaData");
        return spaDataService.findAll();
    }

    /**
     * {@code GET  /spa-data/:id} : get the "id" spaData.
     *
     * @param id the id of the spaData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spaData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spa-data/{id}")
    public ResponseEntity<SpaData> getSpaData(@PathVariable Long id) {
        log.debug("REST request to get SpaData : {}", id);
        Optional<SpaData> spaData = spaDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spaData);
    }

    /**
     * {@code DELETE  /spa-data/:id} : delete the "id" spaData.
     *
     * @param id the id of the spaData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spa-data/{id}")
    public ResponseEntity<Void> deleteSpaData(@PathVariable Long id) {
        log.debug("REST request to delete SpaData : {}", id);
        spaDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
