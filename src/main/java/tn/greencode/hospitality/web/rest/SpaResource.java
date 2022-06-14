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
import tn.greencode.hospitality.domain.Spa;
import tn.greencode.hospitality.repository.SpaRepository;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.Spa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpaResource {

    private final Logger log = LoggerFactory.getLogger(SpaResource.class);

    private static final String ENTITY_NAME = "spa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpaRepository spaRepository;

    public SpaResource(SpaRepository spaRepository) {
        this.spaRepository = spaRepository;
    }

    /**
     * {@code POST  /spas} : Create a new spa.
     *
     * @param spa the spa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spa, or with status {@code 400 (Bad Request)} if the spa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spas")
    public ResponseEntity<Spa> createSpa(@RequestBody Spa spa) throws URISyntaxException {
        log.debug("REST request to save Spa : {}", spa);
        if (spa.getId() != null) {
            throw new BadRequestAlertException("A new spa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spa result = spaRepository.save(spa);
        return ResponseEntity
            .created(new URI("/api/spas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spas/:id} : Updates an existing spa.
     *
     * @param id the id of the spa to save.
     * @param spa the spa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spa,
     * or with status {@code 400 (Bad Request)} if the spa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spas/{id}")
    public ResponseEntity<Spa> updateSpa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Spa spa)
        throws URISyntaxException {
        log.debug("REST request to update Spa : {}, {}", id, spa);
        if (spa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Spa result = spaRepository.save(spa);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spas/:id} : Partial updates given fields of an existing spa, field will ignore if it is null
     *
     * @param id the id of the spa to save.
     * @param spa the spa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spa,
     * or with status {@code 400 (Bad Request)} if the spa is not valid,
     * or with status {@code 404 (Not Found)} if the spa is not found,
     * or with status {@code 500 (Internal Server Error)} if the spa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Spa> partialUpdateSpa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Spa spa)
        throws URISyntaxException {
        log.debug("REST request to partial update Spa partially : {}, {}", id, spa);
        if (spa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Spa> result = spaRepository
            .findById(spa.getId())
            .map(existingSpa -> {
                if (spa.getDate() != null) {
                    existingSpa.setDate(spa.getDate());
                }
                if (spa.getPublish() != null) {
                    existingSpa.setPublish(spa.getPublish());
                }
                if (spa.getContentPosition() != null) {
                    existingSpa.setContentPosition(spa.getContentPosition());
                }
                if (spa.getImagePosition() != null) {
                    existingSpa.setImagePosition(spa.getImagePosition());
                }

                return existingSpa;
            })
            .map(spaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spa.getId().toString())
        );
    }

    /**
     * {@code GET  /spas} : get all the spas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spas in body.
     */
    @GetMapping("/spas")
    public ResponseEntity<List<Spa>> getAllSpas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Spas");
        Page<Spa> page = spaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spas/:id} : get the "id" spa.
     *
     * @param id the id of the spa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spas/{id}")
    public ResponseEntity<Spa> getSpa(@PathVariable Long id) {
        log.debug("REST request to get Spa : {}", id);
        Optional<Spa> spa = spaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(spa);
    }

    /**
     * {@code DELETE  /spas/:id} : delete the "id" spa.
     *
     * @param id the id of the spa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spas/{id}")
    public ResponseEntity<Void> deleteSpa(@PathVariable Long id) {
        log.debug("REST request to delete Spa : {}", id);
        spaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
