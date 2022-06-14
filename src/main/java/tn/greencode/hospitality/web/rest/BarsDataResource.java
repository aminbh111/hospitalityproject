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
import tn.greencode.hospitality.domain.BarsData;
import tn.greencode.hospitality.repository.BarsDataRepository;
import tn.greencode.hospitality.service.BarsDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.BarsData}.
 */
@RestController
@RequestMapping("/api")
public class BarsDataResource {

    private final Logger log = LoggerFactory.getLogger(BarsDataResource.class);

    private static final String ENTITY_NAME = "barsData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BarsDataService barsDataService;

    private final BarsDataRepository barsDataRepository;

    public BarsDataResource(BarsDataService barsDataService, BarsDataRepository barsDataRepository) {
        this.barsDataService = barsDataService;
        this.barsDataRepository = barsDataRepository;
    }

    /**
     * {@code POST  /bars-data} : Create a new barsData.
     *
     * @param barsData the barsData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new barsData, or with status {@code 400 (Bad Request)} if the barsData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bars-data")
    public ResponseEntity<BarsData> createBarsData(@RequestBody BarsData barsData) throws URISyntaxException {
        log.debug("REST request to save BarsData : {}", barsData);
        if (barsData.getId() != null) {
            throw new BadRequestAlertException("A new barsData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BarsData result = barsDataService.save(barsData);
        return ResponseEntity
            .created(new URI("/api/bars-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bars-data/:id} : Updates an existing barsData.
     *
     * @param id the id of the barsData to save.
     * @param barsData the barsData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated barsData,
     * or with status {@code 400 (Bad Request)} if the barsData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the barsData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bars-data/{id}")
    public ResponseEntity<BarsData> updateBarsData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BarsData barsData
    ) throws URISyntaxException {
        log.debug("REST request to update BarsData : {}, {}", id, barsData);
        if (barsData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, barsData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!barsDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BarsData result = barsDataService.update(barsData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, barsData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bars-data/:id} : Partial updates given fields of an existing barsData, field will ignore if it is null
     *
     * @param id the id of the barsData to save.
     * @param barsData the barsData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated barsData,
     * or with status {@code 400 (Bad Request)} if the barsData is not valid,
     * or with status {@code 404 (Not Found)} if the barsData is not found,
     * or with status {@code 500 (Internal Server Error)} if the barsData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bars-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BarsData> partialUpdateBarsData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BarsData barsData
    ) throws URISyntaxException {
        log.debug("REST request to partial update BarsData partially : {}, {}", id, barsData);
        if (barsData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, barsData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!barsDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BarsData> result = barsDataService.partialUpdate(barsData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, barsData.getId().toString())
        );
    }

    /**
     * {@code GET  /bars-data} : get all the barsData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of barsData in body.
     */
    @GetMapping("/bars-data")
    public List<BarsData> getAllBarsData() {
        log.debug("REST request to get all BarsData");
        return barsDataService.findAll();
    }

    /**
     * {@code GET  /bars-data/:id} : get the "id" barsData.
     *
     * @param id the id of the barsData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the barsData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bars-data/{id}")
    public ResponseEntity<BarsData> getBarsData(@PathVariable Long id) {
        log.debug("REST request to get BarsData : {}", id);
        Optional<BarsData> barsData = barsDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(barsData);
    }

    /**
     * {@code DELETE  /bars-data/:id} : delete the "id" barsData.
     *
     * @param id the id of the barsData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bars-data/{id}")
    public ResponseEntity<Void> deleteBarsData(@PathVariable Long id) {
        log.debug("REST request to delete BarsData : {}", id);
        barsDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
