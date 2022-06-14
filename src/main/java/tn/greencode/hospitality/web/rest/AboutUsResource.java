package tn.greencode.hospitality.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.greencode.hospitality.domain.AboutUs;
import tn.greencode.hospitality.repository.AboutUsRepository;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.AboutUs}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AboutUsResource {

    private final Logger log = LoggerFactory.getLogger(AboutUsResource.class);

    private static final String ENTITY_NAME = "aboutUs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AboutUsRepository aboutUsRepository;

    public AboutUsResource(AboutUsRepository aboutUsRepository) {
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
        AboutUs result = aboutUsRepository.save(aboutUs);
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

        AboutUs result = aboutUsRepository.save(aboutUs);
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

        Optional<AboutUs> result = aboutUsRepository
            .findById(aboutUs.getId())
            .map(existingAboutUs -> {
                if (aboutUs.getDate() != null) {
                    existingAboutUs.setDate(aboutUs.getDate());
                }
                if (aboutUs.getPublish() != null) {
                    existingAboutUs.setPublish(aboutUs.getPublish());
                }
                if (aboutUs.getContentPosition() != null) {
                    existingAboutUs.setContentPosition(aboutUs.getContentPosition());
                }
                if (aboutUs.getImagePosition() != null) {
                    existingAboutUs.setImagePosition(aboutUs.getImagePosition());
                }

                return existingAboutUs;
            })
            .map(aboutUsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aboutUs.getId().toString())
        );
    }

    /**
     * {@code GET  /aboutuses} : get all the aboutuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aboutuses in body.
     */
    @GetMapping("/aboutuses")
    public ResponseEntity<List<AboutUs>> getAllAboutuses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Aboutuses");
        Page<AboutUs> page = aboutUsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
        Optional<AboutUs> aboutUs = aboutUsRepository.findById(id);
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
        aboutUsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
