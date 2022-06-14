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
import tn.greencode.hospitality.domain.RoomService;
import tn.greencode.hospitality.repository.RoomServiceRepository;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.RoomService}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RoomServiceResource {

    private final Logger log = LoggerFactory.getLogger(RoomServiceResource.class);

    private static final String ENTITY_NAME = "roomService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomServiceRepository roomServiceRepository;

    public RoomServiceResource(RoomServiceRepository roomServiceRepository) {
        this.roomServiceRepository = roomServiceRepository;
    }

    /**
     * {@code POST  /room-services} : Create a new roomService.
     *
     * @param roomService the roomService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomService, or with status {@code 400 (Bad Request)} if the roomService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-services")
    public ResponseEntity<RoomService> createRoomService(@RequestBody RoomService roomService) throws URISyntaxException {
        log.debug("REST request to save RoomService : {}", roomService);
        if (roomService.getId() != null) {
            throw new BadRequestAlertException("A new roomService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomService result = roomServiceRepository.save(roomService);
        return ResponseEntity
            .created(new URI("/api/room-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-services/:id} : Updates an existing roomService.
     *
     * @param id the id of the roomService to save.
     * @param roomService the roomService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomService,
     * or with status {@code 400 (Bad Request)} if the roomService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-services/{id}")
    public ResponseEntity<RoomService> updateRoomService(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomService roomService
    ) throws URISyntaxException {
        log.debug("REST request to update RoomService : {}, {}", id, roomService);
        if (roomService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoomService result = roomServiceRepository.save(roomService);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomService.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /room-services/:id} : Partial updates given fields of an existing roomService, field will ignore if it is null
     *
     * @param id the id of the roomService to save.
     * @param roomService the roomService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomService,
     * or with status {@code 400 (Bad Request)} if the roomService is not valid,
     * or with status {@code 404 (Not Found)} if the roomService is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/room-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomService> partialUpdateRoomService(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomService roomService
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomService partially : {}, {}", id, roomService);
        if (roomService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomService> result = roomServiceRepository
            .findById(roomService.getId())
            .map(existingRoomService -> {
                if (roomService.getDate() != null) {
                    existingRoomService.setDate(roomService.getDate());
                }
                if (roomService.getPublish() != null) {
                    existingRoomService.setPublish(roomService.getPublish());
                }
                if (roomService.getContentPosition() != null) {
                    existingRoomService.setContentPosition(roomService.getContentPosition());
                }
                if (roomService.getImagePosition() != null) {
                    existingRoomService.setImagePosition(roomService.getImagePosition());
                }

                return existingRoomService;
            })
            .map(roomServiceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomService.getId().toString())
        );
    }

    /**
     * {@code GET  /room-services} : get all the roomServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomServices in body.
     */
    @GetMapping("/room-services")
    public ResponseEntity<List<RoomService>> getAllRoomServices(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RoomServices");
        Page<RoomService> page = roomServiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-services/:id} : get the "id" roomService.
     *
     * @param id the id of the roomService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-services/{id}")
    public ResponseEntity<RoomService> getRoomService(@PathVariable Long id) {
        log.debug("REST request to get RoomService : {}", id);
        Optional<RoomService> roomService = roomServiceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(roomService);
    }

    /**
     * {@code DELETE  /room-services/:id} : delete the "id" roomService.
     *
     * @param id the id of the roomService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-services/{id}")
    public ResponseEntity<Void> deleteRoomService(@PathVariable Long id) {
        log.debug("REST request to delete RoomService : {}", id);
        roomServiceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}