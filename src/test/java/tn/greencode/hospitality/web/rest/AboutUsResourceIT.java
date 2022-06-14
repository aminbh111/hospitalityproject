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
import tn.greencode.hospitality.domain.AboutUs;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.AboutUsRepository;

/**
 * Integration tests for the {@link AboutUsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AboutUsResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/aboutuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AboutUsRepository aboutUsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAboutUsMockMvc;

    private AboutUs aboutUs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutUs createEntity(EntityManager em) {
        AboutUs aboutUs = new AboutUs()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return aboutUs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutUs createUpdatedEntity(EntityManager em) {
        AboutUs aboutUs = new AboutUs()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return aboutUs;
    }

    @BeforeEach
    public void initTest() {
        aboutUs = createEntity(em);
    }

    @Test
    @Transactional
    void createAboutUs() throws Exception {
        int databaseSizeBeforeCreate = aboutUsRepository.findAll().size();
        // Create the AboutUs
        restAboutUsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aboutUs)))
            .andExpect(status().isCreated());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeCreate + 1);
        AboutUs testAboutUs = aboutUsList.get(aboutUsList.size() - 1);
        assertThat(testAboutUs.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAboutUs.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testAboutUs.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testAboutUs.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createAboutUsWithExistingId() throws Exception {
        // Create the AboutUs with an existing ID
        aboutUs.setId(1L);

        int databaseSizeBeforeCreate = aboutUsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAboutUsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aboutUs)))
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAboutuses() throws Exception {
        // Initialize the database
        aboutUsRepository.saveAndFlush(aboutUs);

        // Get all the aboutUsList
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aboutUs.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getAboutUs() throws Exception {
        // Initialize the database
        aboutUsRepository.saveAndFlush(aboutUs);

        // Get the aboutUs
        restAboutUsMockMvc
            .perform(get(ENTITY_API_URL_ID, aboutUs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aboutUs.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAboutUs() throws Exception {
        // Get the aboutUs
        restAboutUsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAboutUs() throws Exception {
        // Initialize the database
        aboutUsRepository.saveAndFlush(aboutUs);

        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();

        // Update the aboutUs
        AboutUs updatedAboutUs = aboutUsRepository.findById(aboutUs.getId()).get();
        // Disconnect from session so that the updates on updatedAboutUs are not directly saved in db
        em.detach(updatedAboutUs);
        updatedAboutUs
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restAboutUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAboutUs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAboutUs))
            )
            .andExpect(status().isOk());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
        AboutUs testAboutUs = aboutUsList.get(aboutUsList.size() - 1);
        assertThat(testAboutUs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAboutUs.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testAboutUs.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testAboutUs.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingAboutUs() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();
        aboutUs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aboutUs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aboutUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAboutUs() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();
        aboutUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aboutUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAboutUs() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();
        aboutUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aboutUs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAboutUsWithPatch() throws Exception {
        // Initialize the database
        aboutUsRepository.saveAndFlush(aboutUs);

        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();

        // Update the aboutUs using partial update
        AboutUs partialUpdatedAboutUs = new AboutUs();
        partialUpdatedAboutUs.setId(aboutUs.getId());

        partialUpdatedAboutUs.imagePosition(UPDATED_IMAGE_POSITION);

        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAboutUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAboutUs))
            )
            .andExpect(status().isOk());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
        AboutUs testAboutUs = aboutUsList.get(aboutUsList.size() - 1);
        assertThat(testAboutUs.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAboutUs.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testAboutUs.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testAboutUs.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateAboutUsWithPatch() throws Exception {
        // Initialize the database
        aboutUsRepository.saveAndFlush(aboutUs);

        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();

        // Update the aboutUs using partial update
        AboutUs partialUpdatedAboutUs = new AboutUs();
        partialUpdatedAboutUs.setId(aboutUs.getId());

        partialUpdatedAboutUs
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAboutUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAboutUs))
            )
            .andExpect(status().isOk());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
        AboutUs testAboutUs = aboutUsList.get(aboutUsList.size() - 1);
        assertThat(testAboutUs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAboutUs.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testAboutUs.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testAboutUs.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingAboutUs() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();
        aboutUs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aboutUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aboutUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAboutUs() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();
        aboutUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aboutUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAboutUs() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsRepository.findAll().size();
        aboutUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aboutUs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AboutUs in the database
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAboutUs() throws Exception {
        // Initialize the database
        aboutUsRepository.saveAndFlush(aboutUs);

        int databaseSizeBeforeDelete = aboutUsRepository.findAll().size();

        // Delete the aboutUs
        restAboutUsMockMvc
            .perform(delete(ENTITY_API_URL_ID, aboutUs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AboutUs> aboutUsList = aboutUsRepository.findAll();
        assertThat(aboutUsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
