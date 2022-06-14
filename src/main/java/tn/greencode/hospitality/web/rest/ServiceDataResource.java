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
import tn.greencode.hospitality.domain.ServiceData;
import tn.greencode.hospitality.repository.ServiceDataRepository;
import tn.greencode.hospitality.service.ServiceDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.ServiceData}.
 */
@RestController
@RequestMapping("/api")
public class ServiceDataResource {

    private final Logger log = LoggerFactory.getLogger(ServiceDataResource.class);

    private static final String ENTITY_NAME = "serviceData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceDataService serviceDataService;

    private final ServiceDataRepository serviceDataRepository;

    public ServiceDataResource(ServiceDataService serviceDataService, ServiceDataRepository serviceDataRepository) {
        this.serviceDataService = serviceDataService;
        this.serviceDataRepository = serviceDataRepository;
    }

    /**
     * {@code POST  /service-data} : Create a new serviceData.
     *
     * @param serviceData the serviceData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceData, or with status {@code 400 (Bad Request)} if the serviceData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-data")
    public ResponseEntity<ServiceData> createServiceData(@RequestBody ServiceData serviceData) throws URISyntaxException {
        log.debug("REST request to save ServiceData : {}", serviceData);
        if (serviceData.getId() != null) {
            throw new BadRequestAlertException("A new serviceData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceData result = serviceDataService.save(serviceData);
        return ResponseEntity
            .created(new URI("/api/service-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-data/:id} : Updates an existing serviceData.
     *
     * @param id the id of the serviceData to save.
     * @param serviceData the serviceData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceData,
     * or with status {@code 400 (Bad Request)} if the serviceData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-data/{id}")
    public ResponseEntity<ServiceData> updateServiceData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceData serviceData
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceData : {}, {}", id, serviceData);
        if (serviceData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceData result = serviceDataService.update(serviceData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-data/:id} : Partial updates given fields of an existing serviceData, field will ignore if it is null
     *
     * @param id the id of the serviceData to save.
     * @param serviceData the serviceData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceData,
     * or with status {@code 400 (Bad Request)} if the serviceData is not valid,
     * or with status {@code 404 (Not Found)} if the serviceData is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceData> partialUpdateServiceData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ServiceData serviceData
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceData partially : {}, {}", id, serviceData);
        if (serviceData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceData> result = serviceDataService.partialUpdate(serviceData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceData.getId().toString())
        );
    }

    /**
     * {@code GET  /service-data} : get all the serviceData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceData in body.
     */
    @GetMapping("/service-data")
    public List<ServiceData> getAllServiceData() {
        log.debug("REST request to get all ServiceData");
        return serviceDataService.findAll();
    }

    /**
     * {@code GET  /service-data/:id} : get the "id" serviceData.
     *
     * @param id the id of the serviceData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-data/{id}")
    public ResponseEntity<ServiceData> getServiceData(@PathVariable Long id) {
        log.debug("REST request to get ServiceData : {}", id);
        Optional<ServiceData> serviceData = serviceDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceData);
    }

    /**
     * {@code DELETE  /service-data/:id} : delete the "id" serviceData.
     *
     * @param id the id of the serviceData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-data/{id}")
    public ResponseEntity<Void> deleteServiceData(@PathVariable Long id) {
        log.debug("REST request to delete ServiceData : {}", id);
        serviceDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
