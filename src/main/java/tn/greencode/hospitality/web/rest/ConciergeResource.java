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
import tn.greencode.hospitality.domain.Concierge;
import tn.greencode.hospitality.repository.ConciergeRepository;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.Concierge}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConciergeResource {

    private final Logger log = LoggerFactory.getLogger(ConciergeResource.class);

    private static final String ENTITY_NAME = "concierge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConciergeRepository conciergeRepository;

    public ConciergeResource(ConciergeRepository conciergeRepository) {
        this.conciergeRepository = conciergeRepository;
    }

    /**
     * {@code POST  /concierges} : Create a new concierge.
     *
     * @param concierge the concierge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new concierge, or with status {@code 400 (Bad Request)} if the concierge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/concierges")
    public ResponseEntity<Concierge> createConcierge(@RequestBody Concierge concierge) throws URISyntaxException {
        log.debug("REST request to save Concierge : {}", concierge);
        if (concierge.getId() != null) {
            throw new BadRequestAlertException("A new concierge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Concierge result = conciergeRepository.save(concierge);
        return ResponseEntity
            .created(new URI("/api/concierges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /concierges/:id} : Updates an existing concierge.
     *
     * @param id the id of the concierge to save.
     * @param concierge the concierge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated concierge,
     * or with status {@code 400 (Bad Request)} if the concierge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the concierge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/concierges/{id}")
    public ResponseEntity<Concierge> updateConcierge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Concierge concierge
    ) throws URISyntaxException {
        log.debug("REST request to update Concierge : {}, {}", id, concierge);
        if (concierge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, concierge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conciergeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Concierge result = conciergeRepository.save(concierge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, concierge.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /concierges/:id} : Partial updates given fields of an existing concierge, field will ignore if it is null
     *
     * @param id the id of the concierge to save.
     * @param concierge the concierge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated concierge,
     * or with status {@code 400 (Bad Request)} if the concierge is not valid,
     * or with status {@code 404 (Not Found)} if the concierge is not found,
     * or with status {@code 500 (Internal Server Error)} if the concierge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/concierges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Concierge> partialUpdateConcierge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Concierge concierge
    ) throws URISyntaxException {
        log.debug("REST request to partial update Concierge partially : {}, {}", id, concierge);
        if (concierge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, concierge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conciergeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Concierge> result = conciergeRepository
            .findById(concierge.getId())
            .map(existingConcierge -> {
                if (concierge.getDate() != null) {
                    existingConcierge.setDate(concierge.getDate());
                }
                if (concierge.getPublish() != null) {
                    existingConcierge.setPublish(concierge.getPublish());
                }
                if (concierge.getContentPosition() != null) {
                    existingConcierge.setContentPosition(concierge.getContentPosition());
                }
                if (concierge.getImagePosition() != null) {
                    existingConcierge.setImagePosition(concierge.getImagePosition());
                }

                return existingConcierge;
            })
            .map(conciergeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, concierge.getId().toString())
        );
    }

    /**
     * {@code GET  /concierges} : get all the concierges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of concierges in body.
     */
    @GetMapping("/concierges")
    public ResponseEntity<List<Concierge>> getAllConcierges(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Concierges");
        Page<Concierge> page = conciergeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /concierges/:id} : get the "id" concierge.
     *
     * @param id the id of the concierge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the concierge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/concierges/{id}")
    public ResponseEntity<Concierge> getConcierge(@PathVariable Long id) {
        log.debug("REST request to get Concierge : {}", id);
        Optional<Concierge> concierge = conciergeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(concierge);
    }

    /**
     * {@code DELETE  /concierges/:id} : delete the "id" concierge.
     *
     * @param id the id of the concierge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/concierges/{id}")
    public ResponseEntity<Void> deleteConcierge(@PathVariable Long id) {
        log.debug("REST request to delete Concierge : {}", id);
        conciergeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
