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
import tn.greencode.hospitality.domain.ContactUs;
import tn.greencode.hospitality.repository.ContactUsRepository;
import tn.greencode.hospitality.service.ContactUsService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.ContactUs}.
 */
@RestController
@RequestMapping("/api")
public class ContactUsResource {

    private final Logger log = LoggerFactory.getLogger(ContactUsResource.class);

    private static final String ENTITY_NAME = "contactUs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactUsService contactUsService;

    private final ContactUsRepository contactUsRepository;

    public ContactUsResource(ContactUsService contactUsService, ContactUsRepository contactUsRepository) {
        this.contactUsService = contactUsService;
        this.contactUsRepository = contactUsRepository;
    }

    /**
     * {@code POST  /contactuses} : Create a new contactUs.
     *
     * @param contactUs the contactUs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactUs, or with status {@code 400 (Bad Request)} if the contactUs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contactuses")
    public ResponseEntity<ContactUs> createContactUs(@RequestBody ContactUs contactUs) throws URISyntaxException {
        log.debug("REST request to save ContactUs : {}", contactUs);
        if (contactUs.getId() != null) {
            throw new BadRequestAlertException("A new contactUs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactUs result = contactUsService.save(contactUs);
        return ResponseEntity
            .created(new URI("/api/contactuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contactuses/:id} : Updates an existing contactUs.
     *
     * @param id the id of the contactUs to save.
     * @param contactUs the contactUs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUs,
     * or with status {@code 400 (Bad Request)} if the contactUs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactUs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contactuses/{id}")
    public ResponseEntity<ContactUs> updateContactUs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactUs contactUs
    ) throws URISyntaxException {
        log.debug("REST request to update ContactUs : {}, {}", id, contactUs);
        if (contactUs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactUs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactUsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactUs result = contactUsService.update(contactUs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUs.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contactuses/:id} : Partial updates given fields of an existing contactUs, field will ignore if it is null
     *
     * @param id the id of the contactUs to save.
     * @param contactUs the contactUs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUs,
     * or with status {@code 400 (Bad Request)} if the contactUs is not valid,
     * or with status {@code 404 (Not Found)} if the contactUs is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactUs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contactuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactUs> partialUpdateContactUs(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactUs contactUs
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactUs partially : {}, {}", id, contactUs);
        if (contactUs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactUs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactUsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactUs> result = contactUsService.partialUpdate(contactUs);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUs.getId().toString())
        );
    }

    /**
     * {@code GET  /contactuses} : get all the contactuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactuses in body.
     */
    @GetMapping("/contactuses")
    public List<ContactUs> getAllContactuses() {
        log.debug("REST request to get all Contactuses");
        return contactUsService.findAll();
    }

    /**
     * {@code GET  /contactuses/:id} : get the "id" contactUs.
     *
     * @param id the id of the contactUs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactUs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contactuses/{id}")
    public ResponseEntity<ContactUs> getContactUs(@PathVariable Long id) {
        log.debug("REST request to get ContactUs : {}", id);
        Optional<ContactUs> contactUs = contactUsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactUs);
    }

    /**
     * {@code DELETE  /contactuses/:id} : delete the "id" contactUs.
     *
     * @param id the id of the contactUs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contactuses/{id}")
    public ResponseEntity<Void> deleteContactUs(@PathVariable Long id) {
        log.debug("REST request to delete ContactUs : {}", id);
        contactUsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}