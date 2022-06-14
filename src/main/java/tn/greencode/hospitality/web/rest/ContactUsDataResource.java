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
import tn.greencode.hospitality.domain.ContactUsData;
import tn.greencode.hospitality.repository.ContactUsDataRepository;
import tn.greencode.hospitality.service.ContactUsDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.ContactUsData}.
 */
@RestController
@RequestMapping("/api")
public class ContactUsDataResource {

    private final Logger log = LoggerFactory.getLogger(ContactUsDataResource.class);

    private static final String ENTITY_NAME = "contactUsData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactUsDataService contactUsDataService;

    private final ContactUsDataRepository contactUsDataRepository;

    public ContactUsDataResource(ContactUsDataService contactUsDataService, ContactUsDataRepository contactUsDataRepository) {
        this.contactUsDataService = contactUsDataService;
        this.contactUsDataRepository = contactUsDataRepository;
    }

    /**
     * {@code POST  /contact-us-data} : Create a new contactUsData.
     *
     * @param contactUsData the contactUsData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactUsData, or with status {@code 400 (Bad Request)} if the contactUsData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-us-data")
    public ResponseEntity<ContactUsData> createContactUsData(@RequestBody ContactUsData contactUsData) throws URISyntaxException {
        log.debug("REST request to save ContactUsData : {}", contactUsData);
        if (contactUsData.getId() != null) {
            throw new BadRequestAlertException("A new contactUsData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactUsData result = contactUsDataService.save(contactUsData);
        return ResponseEntity
            .created(new URI("/api/contact-us-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-us-data/:id} : Updates an existing contactUsData.
     *
     * @param id the id of the contactUsData to save.
     * @param contactUsData the contactUsData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUsData,
     * or with status {@code 400 (Bad Request)} if the contactUsData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactUsData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-us-data/{id}")
    public ResponseEntity<ContactUsData> updateContactUsData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactUsData contactUsData
    ) throws URISyntaxException {
        log.debug("REST request to update ContactUsData : {}, {}", id, contactUsData);
        if (contactUsData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactUsData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactUsDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactUsData result = contactUsDataService.update(contactUsData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUsData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-us-data/:id} : Partial updates given fields of an existing contactUsData, field will ignore if it is null
     *
     * @param id the id of the contactUsData to save.
     * @param contactUsData the contactUsData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUsData,
     * or with status {@code 400 (Bad Request)} if the contactUsData is not valid,
     * or with status {@code 404 (Not Found)} if the contactUsData is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactUsData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-us-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactUsData> partialUpdateContactUsData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContactUsData contactUsData
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactUsData partially : {}, {}", id, contactUsData);
        if (contactUsData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactUsData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactUsDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactUsData> result = contactUsDataService.partialUpdate(contactUsData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUsData.getId().toString())
        );
    }

    /**
     * {@code GET  /contact-us-data} : get all the contactUsData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactUsData in body.
     */
    @GetMapping("/contact-us-data")
    public List<ContactUsData> getAllContactUsData() {
        log.debug("REST request to get all ContactUsData");
        return contactUsDataService.findAll();
    }

    /**
     * {@code GET  /contact-us-data/:id} : get the "id" contactUsData.
     *
     * @param id the id of the contactUsData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactUsData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-us-data/{id}")
    public ResponseEntity<ContactUsData> getContactUsData(@PathVariable Long id) {
        log.debug("REST request to get ContactUsData : {}", id);
        Optional<ContactUsData> contactUsData = contactUsDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactUsData);
    }

    /**
     * {@code DELETE  /contact-us-data/:id} : delete the "id" contactUsData.
     *
     * @param id the id of the contactUsData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-us-data/{id}")
    public ResponseEntity<Void> deleteContactUsData(@PathVariable Long id) {
        log.debug("REST request to delete ContactUsData : {}", id);
        contactUsDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
