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
import tn.greencode.hospitality.domain.ContactUs;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.ContactUsRepository;

/**
 * Integration tests for the {@link ContactUsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactUsResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/contactuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactUsMockMvc;

    private ContactUs contactUs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUs createEntity(EntityManager em) {
        ContactUs contactUs = new ContactUs()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return contactUs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUs createUpdatedEntity(EntityManager em) {
        ContactUs contactUs = new ContactUs()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return contactUs;
    }

    @BeforeEach
    public void initTest() {
        contactUs = createEntity(em);
    }

    @Test
    @Transactional
    void createContactUs() throws Exception {
        int databaseSizeBeforeCreate = contactUsRepository.findAll().size();
        // Create the ContactUs
        restContactUsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isCreated());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeCreate + 1);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testContactUs.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testContactUs.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testContactUs.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createContactUsWithExistingId() throws Exception {
        // Create the ContactUs with an existing ID
        contactUs.setId(1L);

        int databaseSizeBeforeCreate = contactUsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactUsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactuses() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        // Get all the contactUsList
        restContactUsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactUs.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        // Get the contactUs
        restContactUsMockMvc
            .perform(get(ENTITY_API_URL_ID, contactUs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactUs.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContactUs() throws Exception {
        // Get the contactUs
        restContactUsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Update the contactUs
        ContactUs updatedContactUs = contactUsRepository.findById(contactUs.getId()).get();
        // Disconnect from session so that the updates on updatedContactUs are not directly saved in db
        em.detach(updatedContactUs);
        updatedContactUs
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restContactUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactUs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContactUs))
            )
            .andExpect(status().isOk());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testContactUs.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testContactUs.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testContactUs.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();
        contactUs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactUs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();
        contactUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();
        contactUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactUsWithPatch() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Update the contactUs using partial update
        ContactUs partialUpdatedContactUs = new ContactUs();
        partialUpdatedContactUs.setId(contactUs.getId());

        partialUpdatedContactUs.date(UPDATED_DATE).contentPosition(UPDATED_CONTENT_POSITION);

        restContactUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactUs))
            )
            .andExpect(status().isOk());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testContactUs.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testContactUs.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testContactUs.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateContactUsWithPatch() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();

        // Update the contactUs using partial update
        ContactUs partialUpdatedContactUs = new ContactUs();
        partialUpdatedContactUs.setId(contactUs.getId());

        partialUpdatedContactUs
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restContactUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactUs))
            )
            .andExpect(status().isOk());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
        ContactUs testContactUs = contactUsList.get(contactUsList.size() - 1);
        assertThat(testContactUs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testContactUs.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testContactUs.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testContactUs.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();
        contactUs.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactUs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();
        contactUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUs))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactUs() throws Exception {
        int databaseSizeBeforeUpdate = contactUsRepository.findAll().size();
        contactUs.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contactUs))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactUs in the database
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactUs() throws Exception {
        // Initialize the database
        contactUsRepository.saveAndFlush(contactUs);

        int databaseSizeBeforeDelete = contactUsRepository.findAll().size();

        // Delete the contactUs
        restContactUsMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactUs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactUs> contactUsList = contactUsRepository.findAll();
        assertThat(contactUsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
