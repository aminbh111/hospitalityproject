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
import tn.greencode.hospitality.domain.Bars;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.BarsRepository;

/**
 * Integration tests for the {@link BarsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BarsResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/bars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BarsRepository barsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBarsMockMvc;

    private Bars bars;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bars createEntity(EntityManager em) {
        Bars bars = new Bars()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return bars;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bars createUpdatedEntity(EntityManager em) {
        Bars bars = new Bars()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return bars;
    }

    @BeforeEach
    public void initTest() {
        bars = createEntity(em);
    }

    @Test
    @Transactional
    void createBars() throws Exception {
        int databaseSizeBeforeCreate = barsRepository.findAll().size();
        // Create the Bars
        restBarsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bars)))
            .andExpect(status().isCreated());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeCreate + 1);
        Bars testBars = barsList.get(barsList.size() - 1);
        assertThat(testBars.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBars.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testBars.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testBars.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createBarsWithExistingId() throws Exception {
        // Create the Bars with an existing ID
        bars.setId(1L);

        int databaseSizeBeforeCreate = barsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bars)))
            .andExpect(status().isBadRequest());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBars() throws Exception {
        // Initialize the database
        barsRepository.saveAndFlush(bars);

        // Get all the barsList
        restBarsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bars.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getBars() throws Exception {
        // Initialize the database
        barsRepository.saveAndFlush(bars);

        // Get the bars
        restBarsMockMvc
            .perform(get(ENTITY_API_URL_ID, bars.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bars.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBars() throws Exception {
        // Get the bars
        restBarsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBars() throws Exception {
        // Initialize the database
        barsRepository.saveAndFlush(bars);

        int databaseSizeBeforeUpdate = barsRepository.findAll().size();

        // Update the bars
        Bars updatedBars = barsRepository.findById(bars.getId()).get();
        // Disconnect from session so that the updates on updatedBars are not directly saved in db
        em.detach(updatedBars);
        updatedBars
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restBarsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBars.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBars))
            )
            .andExpect(status().isOk());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
        Bars testBars = barsList.get(barsList.size() - 1);
        assertThat(testBars.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBars.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testBars.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testBars.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingBars() throws Exception {
        int databaseSizeBeforeUpdate = barsRepository.findAll().size();
        bars.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bars.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bars))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBars() throws Exception {
        int databaseSizeBeforeUpdate = barsRepository.findAll().size();
        bars.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bars))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBars() throws Exception {
        int databaseSizeBeforeUpdate = barsRepository.findAll().size();
        bars.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bars)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBarsWithPatch() throws Exception {
        // Initialize the database
        barsRepository.saveAndFlush(bars);

        int databaseSizeBeforeUpdate = barsRepository.findAll().size();

        // Update the bars using partial update
        Bars partialUpdatedBars = new Bars();
        partialUpdatedBars.setId(bars.getId());

        partialUpdatedBars.date(UPDATED_DATE).contentPosition(UPDATED_CONTENT_POSITION);

        restBarsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBars.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBars))
            )
            .andExpect(status().isOk());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
        Bars testBars = barsList.get(barsList.size() - 1);
        assertThat(testBars.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBars.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testBars.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testBars.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateBarsWithPatch() throws Exception {
        // Initialize the database
        barsRepository.saveAndFlush(bars);

        int databaseSizeBeforeUpdate = barsRepository.findAll().size();

        // Update the bars using partial update
        Bars partialUpdatedBars = new Bars();
        partialUpdatedBars.setId(bars.getId());

        partialUpdatedBars
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restBarsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBars.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBars))
            )
            .andExpect(status().isOk());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
        Bars testBars = barsList.get(barsList.size() - 1);
        assertThat(testBars.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBars.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testBars.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testBars.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingBars() throws Exception {
        int databaseSizeBeforeUpdate = barsRepository.findAll().size();
        bars.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bars.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bars))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBars() throws Exception {
        int databaseSizeBeforeUpdate = barsRepository.findAll().size();
        bars.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bars))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBars() throws Exception {
        int databaseSizeBeforeUpdate = barsRepository.findAll().size();
        bars.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bars)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bars in the database
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBars() throws Exception {
        // Initialize the database
        barsRepository.saveAndFlush(bars);

        int databaseSizeBeforeDelete = barsRepository.findAll().size();

        // Delete the bars
        restBarsMockMvc
            .perform(delete(ENTITY_API_URL_ID, bars.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bars> barsList = barsRepository.findAll();
        assertThat(barsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
