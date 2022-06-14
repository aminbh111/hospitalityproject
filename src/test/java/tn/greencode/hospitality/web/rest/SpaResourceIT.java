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
import tn.greencode.hospitality.domain.Spa;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.SpaRepository;

/**
 * Integration tests for the {@link SpaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpaResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/spas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpaRepository spaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaMockMvc;

    private Spa spa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spa createEntity(EntityManager em) {
        Spa spa = new Spa()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return spa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spa createUpdatedEntity(EntityManager em) {
        Spa spa = new Spa()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return spa;
    }

    @BeforeEach
    public void initTest() {
        spa = createEntity(em);
    }

    @Test
    @Transactional
    void createSpa() throws Exception {
        int databaseSizeBeforeCreate = spaRepository.findAll().size();
        // Create the Spa
        restSpaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spa)))
            .andExpect(status().isCreated());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeCreate + 1);
        Spa testSpa = spaList.get(spaList.size() - 1);
        assertThat(testSpa.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSpa.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testSpa.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testSpa.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createSpaWithExistingId() throws Exception {
        // Create the Spa with an existing ID
        spa.setId(1L);

        int databaseSizeBeforeCreate = spaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spa)))
            .andExpect(status().isBadRequest());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpas() throws Exception {
        // Initialize the database
        spaRepository.saveAndFlush(spa);

        // Get all the spaList
        restSpaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spa.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getSpa() throws Exception {
        // Initialize the database
        spaRepository.saveAndFlush(spa);

        // Get the spa
        restSpaMockMvc
            .perform(get(ENTITY_API_URL_ID, spa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spa.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSpa() throws Exception {
        // Get the spa
        restSpaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpa() throws Exception {
        // Initialize the database
        spaRepository.saveAndFlush(spa);

        int databaseSizeBeforeUpdate = spaRepository.findAll().size();

        // Update the spa
        Spa updatedSpa = spaRepository.findById(spa.getId()).get();
        // Disconnect from session so that the updates on updatedSpa are not directly saved in db
        em.detach(updatedSpa);
        updatedSpa
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restSpaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpa))
            )
            .andExpect(status().isOk());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
        Spa testSpa = spaList.get(spaList.size() - 1);
        assertThat(testSpa.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSpa.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testSpa.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testSpa.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingSpa() throws Exception {
        int databaseSizeBeforeUpdate = spaRepository.findAll().size();
        spa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spa.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpa() throws Exception {
        int databaseSizeBeforeUpdate = spaRepository.findAll().size();
        spa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpa() throws Exception {
        int databaseSizeBeforeUpdate = spaRepository.findAll().size();
        spa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpaWithPatch() throws Exception {
        // Initialize the database
        spaRepository.saveAndFlush(spa);

        int databaseSizeBeforeUpdate = spaRepository.findAll().size();

        // Update the spa using partial update
        Spa partialUpdatedSpa = new Spa();
        partialUpdatedSpa.setId(spa.getId());

        partialUpdatedSpa.date(UPDATED_DATE).publish(UPDATED_PUBLISH);

        restSpaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpa))
            )
            .andExpect(status().isOk());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
        Spa testSpa = spaList.get(spaList.size() - 1);
        assertThat(testSpa.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSpa.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testSpa.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testSpa.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateSpaWithPatch() throws Exception {
        // Initialize the database
        spaRepository.saveAndFlush(spa);

        int databaseSizeBeforeUpdate = spaRepository.findAll().size();

        // Update the spa using partial update
        Spa partialUpdatedSpa = new Spa();
        partialUpdatedSpa.setId(spa.getId());

        partialUpdatedSpa
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restSpaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpa))
            )
            .andExpect(status().isOk());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
        Spa testSpa = spaList.get(spaList.size() - 1);
        assertThat(testSpa.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSpa.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testSpa.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testSpa.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingSpa() throws Exception {
        int databaseSizeBeforeUpdate = spaRepository.findAll().size();
        spa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpa() throws Exception {
        int databaseSizeBeforeUpdate = spaRepository.findAll().size();
        spa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpa() throws Exception {
        int databaseSizeBeforeUpdate = spaRepository.findAll().size();
        spa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spa in the database
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpa() throws Exception {
        // Initialize the database
        spaRepository.saveAndFlush(spa);

        int databaseSizeBeforeDelete = spaRepository.findAll().size();

        // Delete the spa
        restSpaMockMvc.perform(delete(ENTITY_API_URL_ID, spa.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spa> spaList = spaRepository.findAll();
        assertThat(spaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
