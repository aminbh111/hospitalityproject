package tn.greencode.hospitality.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static tn.greencode.hospitality.web.rest.TestUtil.sameInstant;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.greencode.hospitality.IntegrationTest;
import tn.greencode.hospitality.domain.Concierge;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.ConciergeRepository;

/**
 * Integration tests for the {@link ConciergeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConciergeResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/concierges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConciergeRepository conciergeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConciergeMockMvc;

    private Concierge concierge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concierge createEntity(EntityManager em) {
        Concierge concierge = new Concierge()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return concierge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Concierge createUpdatedEntity(EntityManager em) {
        Concierge concierge = new Concierge()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return concierge;
    }

    @BeforeEach
    public void initTest() {
        concierge = createEntity(em);
    }

    @Test
    @Transactional
    void createConcierge() throws Exception {
        int databaseSizeBeforeCreate = conciergeRepository.findAll().size();
        // Create the Concierge
        restConciergeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concierge)))
            .andExpect(status().isCreated());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeCreate + 1);
        Concierge testConcierge = conciergeList.get(conciergeList.size() - 1);
        assertThat(testConcierge.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testConcierge.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testConcierge.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testConcierge.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createConciergeWithExistingId() throws Exception {
        // Create the Concierge with an existing ID
        concierge.setId(1L);

        int databaseSizeBeforeCreate = conciergeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConciergeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concierge)))
            .andExpect(status().isBadRequest());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConcierges() throws Exception {
        // Initialize the database
        conciergeRepository.saveAndFlush(concierge);

        // Get all the conciergeList
        restConciergeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(concierge.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getConcierge() throws Exception {
        // Initialize the database
        conciergeRepository.saveAndFlush(concierge);

        // Get the concierge
        restConciergeMockMvc
            .perform(get(ENTITY_API_URL_ID, concierge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(concierge.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingConcierge() throws Exception {
        // Get the concierge
        restConciergeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConcierge() throws Exception {
        // Initialize the database
        conciergeRepository.saveAndFlush(concierge);

        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();

        // Update the concierge
        Concierge updatedConcierge = conciergeRepository.findById(concierge.getId()).get();
        // Disconnect from session so that the updates on updatedConcierge are not directly saved in db
        em.detach(updatedConcierge);
        updatedConcierge
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restConciergeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConcierge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConcierge))
            )
            .andExpect(status().isOk());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
        Concierge testConcierge = conciergeList.get(conciergeList.size() - 1);
        assertThat(testConcierge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConcierge.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testConcierge.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testConcierge.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingConcierge() throws Exception {
        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();
        concierge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConciergeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, concierge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concierge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConcierge() throws Exception {
        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();
        concierge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concierge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConcierge() throws Exception {
        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();
        concierge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concierge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConciergeWithPatch() throws Exception {
        // Initialize the database
        conciergeRepository.saveAndFlush(concierge);

        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();

        // Update the concierge using partial update
        Concierge partialUpdatedConcierge = new Concierge();
        partialUpdatedConcierge.setId(concierge.getId());

        partialUpdatedConcierge.date(UPDATED_DATE).publish(UPDATED_PUBLISH).imagePosition(UPDATED_IMAGE_POSITION);

        restConciergeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcierge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConcierge))
            )
            .andExpect(status().isOk());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
        Concierge testConcierge = conciergeList.get(conciergeList.size() - 1);
        assertThat(testConcierge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConcierge.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testConcierge.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testConcierge.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateConciergeWithPatch() throws Exception {
        // Initialize the database
        conciergeRepository.saveAndFlush(concierge);

        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();

        // Update the concierge using partial update
        Concierge partialUpdatedConcierge = new Concierge();
        partialUpdatedConcierge.setId(concierge.getId());

        partialUpdatedConcierge
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restConciergeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcierge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConcierge))
            )
            .andExpect(status().isOk());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
        Concierge testConcierge = conciergeList.get(conciergeList.size() - 1);
        assertThat(testConcierge.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testConcierge.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testConcierge.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testConcierge.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingConcierge() throws Exception {
        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();
        concierge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConciergeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, concierge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concierge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConcierge() throws Exception {
        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();
        concierge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concierge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConcierge() throws Exception {
        int databaseSizeBeforeUpdate = conciergeRepository.findAll().size();
        concierge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(concierge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Concierge in the database
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConcierge() throws Exception {
        // Initialize the database
        conciergeRepository.saveAndFlush(concierge);

        int databaseSizeBeforeDelete = conciergeRepository.findAll().size();

        // Delete the concierge
        restConciergeMockMvc
            .perform(delete(ENTITY_API_URL_ID, concierge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Concierge> conciergeList = conciergeRepository.findAll();
        assertThat(conciergeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
