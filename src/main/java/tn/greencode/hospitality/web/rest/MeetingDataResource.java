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
import tn.greencode.hospitality.domain.MeetingData;
import tn.greencode.hospitality.repository.MeetingDataRepository;
import tn.greencode.hospitality.service.MeetingDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.MeetingData}.
 */
@RestController
@RequestMapping("/api")
public class MeetingDataResource {

    private final Logger log = LoggerFactory.getLogger(MeetingDataResource.class);

    private static final String ENTITY_NAME = "meetingData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeetingDataService meetingDataService;

    private final MeetingDataRepository meetingDataRepository;

    public MeetingDataResource(MeetingDataService meetingDataService, MeetingDataRepository meetingDataRepository) {
        this.meetingDataService = meetingDataService;
        this.meetingDataRepository = meetingDataRepository;
    }

    /**
     * {@code POST  /meeting-data} : Create a new meetingData.
     *
     * @param meetingData the meetingData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meetingData, or with status {@code 400 (Bad Request)} if the meetingData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meeting-data")
    public ResponseEntity<MeetingData> createMeetingData(@RequestBody MeetingData meetingData) throws URISyntaxException {
        log.debug("REST request to save MeetingData : {}", meetingData);
        if (meetingData.getId() != null) {
            throw new BadRequestAlertException("A new meetingData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MeetingData result = meetingDataService.save(meetingData);
        return ResponseEntity
            .created(new URI("/api/meeting-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meeting-data/:id} : Updates an existing meetingData.
     *
     * @param id the id of the meetingData to save.
     * @param meetingData the meetingData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meetingData,
     * or with status {@code 400 (Bad Request)} if the meetingData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meetingData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meeting-data/{id}")
    public ResponseEntity<MeetingData> updateMeetingData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeetingData meetingData
    ) throws URISyntaxException {
        log.debug("REST request to update MeetingData : {}, {}", id, meetingData);
        if (meetingData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MeetingData result = meetingDataService.update(meetingData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meetingData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /meeting-data/:id} : Partial updates given fields of an existing meetingData, field will ignore if it is null
     *
     * @param id the id of the meetingData to save.
     * @param meetingData the meetingData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meetingData,
     * or with status {@code 400 (Bad Request)} if the meetingData is not valid,
     * or with status {@code 404 (Not Found)} if the meetingData is not found,
     * or with status {@code 500 (Internal Server Error)} if the meetingData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/meeting-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeetingData> partialUpdateMeetingData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeetingData meetingData
    ) throws URISyntaxException {
        log.debug("REST request to partial update MeetingData partially : {}, {}", id, meetingData);
        if (meetingData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeetingData> result = meetingDataService.partialUpdate(meetingData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meetingData.getId().toString())
        );
    }

    /**
     * {@code GET  /meeting-data} : get all the meetingData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meetingData in body.
     */
    @GetMapping("/meeting-data")
    public List<MeetingData> getAllMeetingData() {
        log.debug("REST request to get all MeetingData");
        return meetingDataService.findAll();
    }

    /**
     * {@code GET  /meeting-data/:id} : get the "id" meetingData.
     *
     * @param id the id of the meetingData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meetingData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meeting-data/{id}")
    public ResponseEntity<MeetingData> getMeetingData(@PathVariable Long id) {
        log.debug("REST request to get MeetingData : {}", id);
        Optional<MeetingData> meetingData = meetingDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(meetingData);
    }

    /**
     * {@code DELETE  /meeting-data/:id} : delete the "id" meetingData.
     *
     * @param id the id of the meetingData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meeting-data/{id}")
    public ResponseEntity<Void> deleteMeetingData(@PathVariable Long id) {
        log.debug("REST request to delete MeetingData : {}", id);
        meetingDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
