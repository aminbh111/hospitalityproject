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
import tn.greencode.hospitality.domain.AboutUsData;
import tn.greencode.hospitality.repository.AboutUsDataRepository;
import tn.greencode.hospitality.service.AboutUsDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.AboutUsData}.
 */
@RestController
@RequestMapping("/api")
public class AboutUsDataResource {

    private final Logger log = LoggerFactory.getLogger(AboutUsDataResource.class);

    private static final String ENTITY_NAME = "aboutUsData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutUsDataService aboutUsDataService;

    private final AboutUsDataRepository aboutUsDataRepository;

    public AboutUsDataResource(AboutUsDataService aboutUsDataService, AboutUsDataRepository aboutUsDataRepository) {
        this.aboutUsDataService = aboutUsDataService;
        this.aboutUsDataRepository = aboutUsDataRepository;
    }

    /**
     * {@code POST  /about-us-data} : Create a new aboutUsData.
     *
     * @param aboutUsData the aboutUsData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aboutUsData, or with status {@code 400 (Bad Request)} if the aboutUsData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/about-us-data")
    public ResponseEntity<AboutUsData> createAboutUsData(@RequestBody AboutUsData aboutUsData) throws URISyntaxException {
        log.debug("REST request to save AboutUsData : {}", aboutUsData);
        if (aboutUsData.getId() != null) {
            throw new BadRequestAlertException("A new aboutUsData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AboutUsData result = aboutUsDataService.save(aboutUsData);
        return ResponseEntity
            .created(new URI("/api/about-us-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /about-us-data/:id} : Updates an existing aboutUsData.
     *
     * @param id the id of the aboutUsData to save.
     * @param aboutUsData the aboutUsData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutUsData,
     * or with status {@code 400 (Bad Request)} if the aboutUsData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aboutUsData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/about-us-data/{id}")
    public ResponseEntity<AboutUsData> updateAboutUsData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AboutUsData aboutUsData
    ) throws URISyntaxException {
        log.debug("REST request to update AboutUsData : {}, {}", id, aboutUsData);
        if (aboutUsData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutUsData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutUsDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AboutUsData result = aboutUsDataService.update(aboutUsData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUsData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /about-us-data/:id} : Partial updates given fields of an existing aboutUsData, field will ignore if it is null
     *
     * @param id the id of the aboutUsData to save.
     * @param aboutUsData the aboutUsData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutUsData,
     * or with status {@code 400 (Bad Request)} if the aboutUsData is not valid,
     * or with status {@code 404 (Not Found)} if the aboutUsData is not found,
     * or with status {@code 500 (Internal Server Error)} if the aboutUsData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/about-us-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AboutUsData> partialUpdateAboutUsData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AboutUsData aboutUsData
    ) throws URISyntaxException {
        log.debug("REST request to partial update AboutUsData partially : {}, {}", id, aboutUsData);
        if (aboutUsData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutUsData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutUsDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AboutUsData> result = aboutUsDataService.partialUpdate(aboutUsData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUsData.getId().toString())
        );
    }

    /**
     * {@code GET  /about-us-data} : get all the aboutUsData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutUsData in body.
     */
    @GetMapping("/about-us-data")
    public List<AboutUsData> getAllAboutUsData() {
        log.debug("REST request to get all AboutUsData");
        return aboutUsDataService.findAll();
    }

    /**
     * {@code GET  /about-us-data/:id} : get the "id" aboutUsData.
     *
     * @param id the id of the aboutUsData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutUsData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/about-us-data/{id}")
    public ResponseEntity<AboutUsData> getAboutUsData(@PathVariable Long id) {
        log.debug("REST request to get AboutUsData : {}", id);
        Optional<AboutUsData> aboutUsData = aboutUsDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aboutUsData);
    }

    /**
     * {@code DELETE  /about-us-data/:id} : delete the "id" aboutUsData.
     *
     * @param id the id of the aboutUsData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/about-us-data/{id}")
    public ResponseEntity<Void> deleteAboutUsData(@PathVariable Long id) {
        log.debug("REST request to delete AboutUsData : {}", id);
        aboutUsDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
