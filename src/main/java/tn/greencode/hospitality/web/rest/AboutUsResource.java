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
import tn.greencode.hospitality.domain.AboutUs;
import tn.greencode.hospitality.repository.AboutUsRepository;
import tn.greencode.hospitality.service.AboutUsService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.AboutUs}.
 */
@RestController
@RequestMapping("/api")
public class AboutUsResource {

    private final Logger log = LoggerFactory.getLogger(AboutUsResource.class);

    private static final String ENTITY_NAME = "aboutUs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutUsService aboutUsService;

    private final AboutUsRepository aboutUsRepository;

    public AboutUsResource(AboutUsService aboutUsService, AboutUsRepository aboutUsRepository) {
        this.aboutUsService = aboutUsService;
        this.aboutUsRepository = aboutUsRepository;
    }

    /**
     * {@code POST  /aboutuses} : Create a new aboutUs.
     *
     * @param aboutUs the aboutUs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aboutUs, or with status {@code 400 (Bad Request)} if the aboutUs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aboutuses")
    public ResponseEntity<AboutUs> createAboutUs(@RequestBody AboutUs aboutUs) throws URISyntaxException {
        log.debug("REST request to save AboutUs : {}", aboutUs);
        if (aboutUs.getId() != null) {
            throw new BadRequestAlertException("A new aboutUs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AboutUs result = aboutUsService.save(aboutUs);
        return ResponseEntity
            .created(new URI("/api/aboutuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aboutuses/:id} : Updates an existing aboutUs.
     *
     * @param id the id of the aboutUs to save.
     * @param aboutUs the aboutUs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutUs,
     * or with status {@code 400 (Bad Request)} if the aboutUs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aboutUs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aboutuses/{id}")
    public ResponseEntity<AboutUs> updateAboutUs(@PathVariable(value = "id", required = false) final Long id, @RequestBody AboutUs aboutUs)
        throws URISyntaxException {
        log.debug("REST request to update AboutUs : {}, {}", id, aboutUs);
        if (aboutUs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutUs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutUsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AboutUs result = aboutUsService.update(aboutUs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aboutuses/:id} : Partial updates given fields of an existing aboutUs, field will ignore if it is null
     *
     * @param id the id of the aboutUs to save.
     * @param aboutUs the aboutUs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aboutUs,
     * or with status {@code 400 (Bad Request)} if the aboutUs is not valid,
     * or with status {@code 404 (Not Found)} if the aboutUs is not found,
     * or with status {@code 500 (Internal Server Error)} if the aboutUs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aboutuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AboutUs> partialUpdateAboutUs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AboutUs aboutUs
    ) throws URISyntaxException {
        log.debug("REST request to partial update AboutUs partially : {}, {}", id, aboutUs);
        if (aboutUs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aboutUs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aboutUsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AboutUs> result = aboutUsService.partialUpdate(aboutUs);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUs.getId().toString())
        );
    }

    /**
     * {@code GET  /aboutuses} : get all the aboutuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutuses in body.
     */
    @GetMapping("/aboutuses")
    public List<AboutUs> getAllAboutuses() {
        log.debug("REST request to get all Aboutuses");
        return aboutUsService.findAll();
    }

    /**
     * {@code GET  /aboutuses/:id} : get the "id" aboutUs.
     *
     * @param id the id of the aboutUs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aboutUs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aboutuses/{id}")
    public ResponseEntity<AboutUs> getAboutUs(@PathVariable Long id) {
        log.debug("REST request to get AboutUs : {}", id);
        Optional<AboutUs> aboutUs = aboutUsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aboutUs);
    }

    /**
     * {@code DELETE  /aboutuses/:id} : delete the "id" aboutUs.
     *
     * @param id the id of the aboutUs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aboutuses/{id}")
    public ResponseEntity<Void> deleteAboutUs(@PathVariable Long id) {
        log.debug("REST request to delete AboutUs : {}", id);
        aboutUsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
