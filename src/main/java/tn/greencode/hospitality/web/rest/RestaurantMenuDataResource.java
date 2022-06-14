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
import tn.greencode.hospitality.domain.RestaurantMenuData;
import tn.greencode.hospitality.repository.RestaurantMenuDataRepository;
import tn.greencode.hospitality.service.RestaurantMenuDataService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.RestaurantMenuData}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantMenuDataResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantMenuDataResource.class);

    private static final String ENTITY_NAME = "restaurantMenuData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantMenuDataService restaurantMenuDataService;

    private final RestaurantMenuDataRepository restaurantMenuDataRepository;

    public RestaurantMenuDataResource(
        RestaurantMenuDataService restaurantMenuDataService,
        RestaurantMenuDataRepository restaurantMenuDataRepository
    ) {
        this.restaurantMenuDataService = restaurantMenuDataService;
        this.restaurantMenuDataRepository = restaurantMenuDataRepository;
    }

    /**
     * {@code POST  /restaurant-menu-data} : Create a new restaurantMenuData.
     *
     * @param restaurantMenuData the restaurantMenuData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantMenuData, or with status {@code 400 (Bad Request)} if the restaurantMenuData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurant-menu-data")
    public ResponseEntity<RestaurantMenuData> createRestaurantMenuData(@RequestBody RestaurantMenuData restaurantMenuData)
        throws URISyntaxException {
        log.debug("REST request to save RestaurantMenuData : {}", restaurantMenuData);
        if (restaurantMenuData.getId() != null) {
            throw new BadRequestAlertException("A new restaurantMenuData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurantMenuData result = restaurantMenuDataService.save(restaurantMenuData);
        return ResponseEntity
            .created(new URI("/api/restaurant-menu-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurant-menu-data/:id} : Updates an existing restaurantMenuData.
     *
     * @param id the id of the restaurantMenuData to save.
     * @param restaurantMenuData the restaurantMenuData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantMenuData,
     * or with status {@code 400 (Bad Request)} if the restaurantMenuData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantMenuData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurant-menu-data/{id}")
    public ResponseEntity<RestaurantMenuData> updateRestaurantMenuData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantMenuData restaurantMenuData
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantMenuData : {}, {}", id, restaurantMenuData);
        if (restaurantMenuData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantMenuData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantMenuDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurantMenuData result = restaurantMenuDataService.update(restaurantMenuData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantMenuData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurant-menu-data/:id} : Partial updates given fields of an existing restaurantMenuData, field will ignore if it is null
     *
     * @param id the id of the restaurantMenuData to save.
     * @param restaurantMenuData the restaurantMenuData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantMenuData,
     * or with status {@code 400 (Bad Request)} if the restaurantMenuData is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantMenuData is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantMenuData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurant-menu-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurantMenuData> partialUpdateRestaurantMenuData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantMenuData restaurantMenuData
    ) throws URISyntaxException {
        log.debug("REST request to partial update RestaurantMenuData partially : {}, {}", id, restaurantMenuData);
        if (restaurantMenuData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantMenuData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantMenuDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurantMenuData> result = restaurantMenuDataService.partialUpdate(restaurantMenuData);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantMenuData.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurant-menu-data} : get all the restaurantMenuData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantMenuData in body.
     */
    @GetMapping("/restaurant-menu-data")
    public List<RestaurantMenuData> getAllRestaurantMenuData() {
        log.debug("REST request to get all RestaurantMenuData");
        return restaurantMenuDataService.findAll();
    }

    /**
     * {@code GET  /restaurant-menu-data/:id} : get the "id" restaurantMenuData.
     *
     * @param id the id of the restaurantMenuData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantMenuData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurant-menu-data/{id}")
    public ResponseEntity<RestaurantMenuData> getRestaurantMenuData(@PathVariable Long id) {
        log.debug("REST request to get RestaurantMenuData : {}", id);
        Optional<RestaurantMenuData> restaurantMenuData = restaurantMenuDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurantMenuData);
    }

    /**
     * {@code DELETE  /restaurant-menu-data/:id} : delete the "id" restaurantMenuData.
     *
     * @param id the id of the restaurantMenuData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurant-menu-data/{id}")
    public ResponseEntity<Void> deleteRestaurantMenuData(@PathVariable Long id) {
        log.debug("REST request to delete RestaurantMenuData : {}", id);
        restaurantMenuDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
