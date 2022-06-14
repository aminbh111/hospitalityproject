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
import tn.greencode.hospitality.domain.Bars;
import tn.greencode.hospitality.repository.BarsRepository;
import tn.greencode.hospitality.service.BarsService;
import tn.greencode.hospitality.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.greencode.hospitality.domain.Bars}.
 */
@RestController
@RequestMapping("/api")
public class BarsResource {

    private final Logger log = LoggerFactory.getLogger(BarsResource.class);

    private static final String ENTITY_NAME = "bars";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BarsService barsService;

    private final BarsRepository barsRepository;

    public BarsResource(BarsService barsService, BarsRepository barsRepository) {
        this.barsService = barsService;
        this.barsRepository = barsRepository;
    }

    /**
     * {@code POST  /bars} : Create a new bars.
     *
     * @param bars the bars to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bars, or with status {@code 400 (Bad Request)} if the bars has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bars")
    public ResponseEntity<Bars> createBars(@RequestBody Bars bars) throws URISyntaxException {
        log.debug("REST request to save Bars : {}", bars);
        if (bars.getId() != null) {
            throw new BadRequestAlertException("A new bars cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bars result = barsService.save(bars);
        return ResponseEntity
            .created(new URI("/api/bars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bars/:id} : Updates an existing bars.
     *
     * @param id the id of the bars to save.
     * @param bars the bars to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bars,
     * or with status {@code 400 (Bad Request)} if the bars is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bars couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bars/{id}")
    public ResponseEntity<Bars> updateBars(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bars bars)
        throws URISyntaxException {
        log.debug("REST request to update Bars : {}, {}", id, bars);
        if (bars.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bars.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!barsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bars result = barsService.update(bars);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bars.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bars/:id} : Partial updates given fields of an existing bars, field will ignore if it is null
     *
     * @param id the id of the bars to save.
     * @param bars the bars to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bars,
     * or with status {@code 400 (Bad Request)} if the bars is not valid,
     * or with status {@code 404 (Not Found)} if the bars is not found,
     * or with status {@code 500 (Internal Server Error)} if the bars couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bars> partialUpdateBars(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bars bars)
        throws URISyntaxException {
        log.debug("REST request to partial update Bars partially : {}, {}", id, bars);
        if (bars.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bars.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!barsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bars> result = barsService.partialUpdate(bars);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bars.getId().toString())
        );
    }

    /**
     * {@code GET  /bars} : get all the bars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bars in body.
     */
    @GetMapping("/bars")
    public List<Bars> getAllBars() {
        log.debug("REST request to get all Bars");
        return barsService.findAll();
    }

    /**
     * {@code GET  /bars/:id} : get the "id" bars.
     *
     * @param id the id of the bars to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bars, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bars/{id}")
    public ResponseEntity<Bars> getBars(@PathVariable Long id) {
        log.debug("REST request to get Bars : {}", id);
        Optional<Bars> bars = barsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bars);
    }

    /**
     * {@code DELETE  /bars/:id} : delete the "id" bars.
     *
     * @param id the id of the bars to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bars/{id}")
    public ResponseEntity<Void> deleteBars(@PathVariable Long id) {
        log.debug("REST request to delete Bars : {}", id);
        barsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
