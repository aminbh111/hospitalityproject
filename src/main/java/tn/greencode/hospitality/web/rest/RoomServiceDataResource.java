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
import tn.greencode.hospitality.domain.RoomServiceData;
import tn.greencode.hospitality.repository.RoomServiceDataRepository;
import tn.greencode.hospitality.service.RoomServiceDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.RoomServiceData}.
 */
@RestController
@RequestMapping("/api")
public class RoomServiceDataResource {

    private final Logger log = LoggerFactory.getLogger(RoomServiceDataResource.class);

    private static final String ENTITY_NAME = "roomServiceData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomServiceDataService roomServiceDataService;

    private final RoomServiceDataRepository roomServiceDataRepository;

    public RoomServiceDataResource(RoomServiceDataService roomServiceDataService, RoomServiceDataRepository roomServiceDataRepository) {
        this.roomServiceDataService = roomServiceDataService;
        this.roomServiceDataRepository = roomServiceDataRepository;
    }

    /**
     * {@code POST  /room-service-data} : Create a new roomServiceData.
     *
     * @param roomServiceData the roomServiceData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomServiceData, or with status {@code 400 (Bad Request)} if the roomServiceData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-service-data")
    public ResponseEntity<RoomServiceData> createRoomServiceData(@RequestBody RoomServiceData roomServiceData) throws URISyntaxException {
        log.debug("REST request to save RoomServiceData : {}", roomServiceData);
        if (roomServiceData.getId() != null) {
            throw new BadRequestAlertException("A new roomServiceData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomServiceData result = roomServiceDataService.save(roomServiceData);
        return ResponseEntity
            .created(new URI("/api/room-service-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-service-data/:id} : Updates an existing roomServiceData.
     *
     * @param id the id of the roomServiceData to save.
     * @param roomServiceData the roomServiceData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomServiceData,
     * or with status {@code 400 (Bad Request)} if the roomServiceData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomServiceData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-service-data/{id}")
    public ResponseEntity<RoomServiceData> updateRoomServiceData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomServiceData roomServiceData
    ) throws URISyntaxException {
        log.debug("REST request to update RoomServiceData : {}, {}", id, roomServiceData);
        if (roomServiceData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomServiceData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomServiceDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoomServiceData result = roomServiceDataService.update(roomServiceData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomServiceData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /room-service-data/:id} : Partial updates given fields of an existing roomServiceData, field will ignore if it is null
     *
     * @param id the id of the roomServiceData to save.
     * @param roomServiceData the roomServiceData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomServiceData,
     * or with status {@code 400 (Bad Request)} if the roomServiceData is not valid,
     * or with status {@code 404 (Not Found)} if the roomServiceData is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomServiceData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/room-service-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomServiceData> partialUpdateRoomServiceData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomServiceData roomServiceData
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomServiceData partially : {}, {}", id, roomServiceData);
        if (roomServiceData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomServiceData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomServiceDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomServiceData> result = roomServiceDataService.partialUpdate(roomServiceData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomServiceData.getId().toString())
        );
    }

    /**
     * {@code GET  /room-service-data} : get all the roomServiceData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomServiceData in body.
     */
    @GetMapping("/room-service-data")
    public List<RoomServiceData> getAllRoomServiceData() {
        log.debug("REST request to get all RoomServiceData");
        return roomServiceDataService.findAll();
    }

    /**
     * {@code GET  /room-service-data/:id} : get the "id" roomServiceData.
     *
     * @param id the id of the roomServiceData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomServiceData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-service-data/{id}")
    public ResponseEntity<RoomServiceData> getRoomServiceData(@PathVariable Long id) {
        log.debug("REST request to get RoomServiceData : {}", id);
        Optional<RoomServiceData> roomServiceData = roomServiceDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomServiceData);
    }

    /**
     * {@code DELETE  /room-service-data/:id} : delete the "id" roomServiceData.
     *
     * @param id the id of the roomServiceData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-service-data/{id}")
    public ResponseEntity<Void> deleteRoomServiceData(@PathVariable Long id) {
        log.debug("REST request to delete RoomServiceData : {}", id);
        roomServiceDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
