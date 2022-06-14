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
import tn.greencode.hospitality.domain.EventData;
import tn.greencode.hospitality.repository.EventDataRepository;
import tn.greencode.hospitality.service.EventDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.EventData}.
 */
@RestController
@RequestMapping("/api")
public class EventDataResource {

    private final Logger log = LoggerFactory.getLogger(EventDataResource.class);

    private static final String ENTITY_NAME = "eventData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventDataService eventDataService;

    private final EventDataRepository eventDataRepository;

    public EventDataResource(EventDataService eventDataService, EventDataRepository eventDataRepository) {
        this.eventDataService = eventDataService;
        this.eventDataRepository = eventDataRepository;
    }

    /**
     * {@code POST  /event-data} : Create a new eventData.
     *
     * @param eventData the eventData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventData, or with status {@code 400 (Bad Request)} if the eventData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-data")
    public ResponseEntity<EventData> createEventData(@RequestBody EventData eventData) throws URISyntaxException {
        log.debug("REST request to save EventData : {}", eventData);
        if (eventData.getId() != null) {
            throw new BadRequestAlertException("A new eventData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventData result = eventDataService.save(eventData);
        return ResponseEntity
            .created(new URI("/api/event-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-data/:id} : Updates an existing eventData.
     *
     * @param id the id of the eventData to save.
     * @param eventData the eventData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventData,
     * or with status {@code 400 (Bad Request)} if the eventData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-data/{id}")
    public ResponseEntity<EventData> updateEventData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventData eventData
    ) throws URISyntaxException {
        log.debug("REST request to update EventData : {}, {}", id, eventData);
        if (eventData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventData result = eventDataService.update(eventData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-data/:id} : Partial updates given fields of an existing eventData, field will ignore if it is null
     *
     * @param id the id of the eventData to save.
     * @param eventData the eventData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventData,
     * or with status {@code 400 (Bad Request)} if the eventData is not valid,
     * or with status {@code 404 (Not Found)} if the eventData is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventData> partialUpdateEventData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventData eventData
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventData partially : {}, {}", id, eventData);
        if (eventData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventData> result = eventDataService.partialUpdate(eventData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventData.getId().toString())
        );
    }

    /**
     * {@code GET  /event-data} : get all the eventData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventData in body.
     */
    @GetMapping("/event-data")
    public List<EventData> getAllEventData() {
        log.debug("REST request to get all EventData");
        return eventDataService.findAll();
    }

    /**
     * {@code GET  /event-data/:id} : get the "id" eventData.
     *
     * @param id the id of the eventData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-data/{id}")
    public ResponseEntity<EventData> getEventData(@PathVariable Long id) {
        log.debug("REST request to get EventData : {}", id);
        Optional<EventData> eventData = eventDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventData);
    }

    /**
     * {@code DELETE  /event-data/:id} : delete the "id" eventData.
     *
     * @param id the id of the eventData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-data/{id}")
    public ResponseEntity<Void> deleteEventData(@PathVariable Long id) {
        log.debug("REST request to delete EventData : {}", id);
        eventDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
