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
import tn.greencode.hospitality.domain.ConciergeData;
import tn.greencode.hospitality.repository.ConciergeDataRepository;
import tn.greencode.hospitality.service.ConciergeDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.ConciergeData}.
 */
@RestController
@RequestMapping("/api")
public class ConciergeDataResource {

    private final Logger log = LoggerFactory.getLogger(ConciergeDataResource.class);

    private static final String ENTITY_NAME = "conciergeData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConciergeDataService conciergeDataService;

    private final ConciergeDataRepository conciergeDataRepository;

    public ConciergeDataResource(ConciergeDataService conciergeDataService, ConciergeDataRepository conciergeDataRepository) {
        this.conciergeDataService = conciergeDataService;
        this.conciergeDataRepository = conciergeDataRepository;
    }

    /**
     * {@code POST  /concierge-data} : Create a new conciergeData.
     *
     * @param conciergeData the conciergeData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conciergeData, or with status {@code 400 (Bad Request)} if the conciergeData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/concierge-data")
    public ResponseEntity<ConciergeData> createConciergeData(@RequestBody ConciergeData conciergeData) throws URISyntaxException {
        log.debug("REST request to save ConciergeData : {}", conciergeData);
        if (conciergeData.getId() != null) {
            throw new BadRequestAlertException("A new conciergeData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConciergeData result = conciergeDataService.save(conciergeData);
        return ResponseEntity
            .created(new URI("/api/concierge-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /concierge-data/:id} : Updates an existing conciergeData.
     *
     * @param id the id of the conciergeData to save.
     * @param conciergeData the conciergeData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conciergeData,
     * or with status {@code 400 (Bad Request)} if the conciergeData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conciergeData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/concierge-data/{id}")
    public ResponseEntity<ConciergeData> updateConciergeData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConciergeData conciergeData
    ) throws URISyntaxException {
        log.debug("REST request to update ConciergeData : {}, {}", id, conciergeData);
        if (conciergeData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conciergeData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conciergeDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConciergeData result = conciergeDataService.update(conciergeData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conciergeData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /concierge-data/:id} : Partial updates given fields of an existing conciergeData, field will ignore if it is null
     *
     * @param id the id of the conciergeData to save.
     * @param conciergeData the conciergeData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conciergeData,
     * or with status {@code 400 (Bad Request)} if the conciergeData is not valid,
     * or with status {@code 404 (Not Found)} if the conciergeData is not found,
     * or with status {@code 500 (Internal Server Error)} if the conciergeData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/concierge-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConciergeData> partialUpdateConciergeData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConciergeData conciergeData
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConciergeData partially : {}, {}", id, conciergeData);
        if (conciergeData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conciergeData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conciergeDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConciergeData> result = conciergeDataService.partialUpdate(conciergeData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conciergeData.getId().toString())
        );
    }

    /**
     * {@code GET  /concierge-data} : get all the conciergeData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conciergeData in body.
     */
    @GetMapping("/concierge-data")
    public List<ConciergeData> getAllConciergeData() {
        log.debug("REST request to get all ConciergeData");
        return conciergeDataService.findAll();
    }

    /**
     * {@code GET  /concierge-data/:id} : get the "id" conciergeData.
     *
     * @param id the id of the conciergeData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conciergeData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/concierge-data/{id}")
    public ResponseEntity<ConciergeData> getConciergeData(@PathVariable Long id) {
        log.debug("REST request to get ConciergeData : {}", id);
        Optional<ConciergeData> conciergeData = conciergeDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conciergeData);
    }

    /**
     * {@code DELETE  /concierge-data/:id} : delete the "id" conciergeData.
     *
     * @param id the id of the conciergeData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/concierge-data/{id}")
    public ResponseEntity<Void> deleteConciergeData(@PathVariable Long id) {
        log.debug("REST request to delete ConciergeData : {}", id);
        conciergeDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
