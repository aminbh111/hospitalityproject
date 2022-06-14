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
import tn.greencode.hospitality.domain.OfferData;
import tn.greencode.hospitality.repository.OfferDataRepository;
import tn.greencode.hospitality.service.OfferDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.OfferData}.
 */
@RestController
@RequestMapping("/api")
public class OfferDataResource {

    private final Logger log = LoggerFactory.getLogger(OfferDataResource.class);

    private static final String ENTITY_NAME = "offerData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferDataService offerDataService;

    private final OfferDataRepository offerDataRepository;

    public OfferDataResource(OfferDataService offerDataService, OfferDataRepository offerDataRepository) {
        this.offerDataService = offerDataService;
        this.offerDataRepository = offerDataRepository;
    }

    /**
     * {@code POST  /offer-data} : Create a new offerData.
     *
     * @param offerData the offerData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offerData, or with status {@code 400 (Bad Request)} if the offerData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/offer-data")
    public ResponseEntity<OfferData> createOfferData(@RequestBody OfferData offerData) throws URISyntaxException {
        log.debug("REST request to save OfferData : {}", offerData);
        if (offerData.getId() != null) {
            throw new BadRequestAlertException("A new offerData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfferData result = offerDataService.save(offerData);
        return ResponseEntity
            .created(new URI("/api/offer-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /offer-data/:id} : Updates an existing offerData.
     *
     * @param id the id of the offerData to save.
     * @param offerData the offerData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerData,
     * or with status {@code 400 (Bad Request)} if the offerData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offerData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/offer-data/{id}")
    public ResponseEntity<OfferData> updateOfferData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OfferData offerData
    ) throws URISyntaxException {
        log.debug("REST request to update OfferData : {}, {}", id, offerData);
        if (offerData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offerDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OfferData result = offerDataService.update(offerData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offerData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /offer-data/:id} : Partial updates given fields of an existing offerData, field will ignore if it is null
     *
     * @param id the id of the offerData to save.
     * @param offerData the offerData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerData,
     * or with status {@code 400 (Bad Request)} if the offerData is not valid,
     * or with status {@code 404 (Not Found)} if the offerData is not found,
     * or with status {@code 500 (Internal Server Error)} if the offerData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/offer-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OfferData> partialUpdateOfferData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OfferData offerData
    ) throws URISyntaxException {
        log.debug("REST request to partial update OfferData partially : {}, {}", id, offerData);
        if (offerData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offerDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OfferData> result = offerDataService.partialUpdate(offerData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offerData.getId().toString())
        );
    }

    /**
     * {@code GET  /offer-data} : get all the offerData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offerData in body.
     */
    @GetMapping("/offer-data")
    public List<OfferData> getAllOfferData() {
        log.debug("REST request to get all OfferData");
        return offerDataService.findAll();
    }

    /**
     * {@code GET  /offer-data/:id} : get the "id" offerData.
     *
     * @param id the id of the offerData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offerData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/offer-data/{id}")
    public ResponseEntity<OfferData> getOfferData(@PathVariable Long id) {
        log.debug("REST request to get OfferData : {}", id);
        Optional<OfferData> offerData = offerDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offerData);
    }

    /**
     * {@code DELETE  /offer-data/:id} : delete the "id" offerData.
     *
     * @param id the id of the offerData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/offer-data/{id}")
    public ResponseEntity<Void> deleteOfferData(@PathVariable Long id) {
        log.debug("REST request to delete OfferData : {}", id);
        offerDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
