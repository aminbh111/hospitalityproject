package tn.greencode.hospitality.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.util.Base64Utils;
import tn.greencode.hospitality.IntegrationTest;
import tn.greencode.hospitality.domain.ContactUsData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.ContactUsDataRepository;

/**
 * Integration tests for the {@link ContactUsDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactUsDataResourceIT {

    private static final Language DEFAULT_LANG = Language.FR;
    private static final Language UPDATED_LANG = Language.EN;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/contact-us-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactUsDataRepository contactUsDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactUsDataMockMvc;

    private ContactUsData contactUsData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUsData createEntity(EntityManager em) {
        ContactUsData contactUsData = new ContactUsData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return contactUsData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactUsData createUpdatedEntity(EntityManager em) {
        ContactUsData contactUsData = new ContactUsData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return contactUsData;
    }

    @BeforeEach
    public void initTest() {
        contactUsData = createEntity(em);
    }

    @Test
    @Transactional
    void createContactUsData() throws Exception {
        int databaseSizeBeforeCreate = contactUsDataRepository.findAll().size();
        // Create the ContactUsData
        restContactUsDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUsData)))
            .andExpect(status().isCreated());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeCreate + 1);
        ContactUsData testContactUsData = contactUsDataList.get(contactUsDataList.size() - 1);
        assertThat(testContactUsData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testContactUsData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContactUsData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testContactUsData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testContactUsData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createContactUsDataWithExistingId() throws Exception {
        // Create the ContactUsData with an existing ID
        contactUsData.setId(1L);

        int databaseSizeBeforeCreate = contactUsDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactUsDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUsData)))
            .andExpect(status().isBadRequest());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContactUsData() throws Exception {
        // Initialize the database
        contactUsDataRepository.saveAndFlush(contactUsData);

        // Get all the contactUsDataList
        restContactUsDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactUsData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getContactUsData() throws Exception {
        // Initialize the database
        contactUsDataRepository.saveAndFlush(contactUsData);

        // Get the contactUsData
        restContactUsDataMockMvc
            .perform(get(ENTITY_API_URL_ID, contactUsData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactUsData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingContactUsData() throws Exception {
        // Get the contactUsData
        restContactUsDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContactUsData() throws Exception {
        // Initialize the database
        contactUsDataRepository.saveAndFlush(contactUsData);

        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();

        // Update the contactUsData
        ContactUsData updatedContactUsData = contactUsDataRepository.findById(contactUsData.getId()).get();
        // Disconnect from session so that the updates on updatedContactUsData are not directly saved in db
        em.detach(updatedContactUsData);
        updatedContactUsData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restContactUsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContactUsData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContactUsData))
            )
            .andExpect(status().isOk());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
        ContactUsData testContactUsData = contactUsDataList.get(contactUsDataList.size() - 1);
        assertThat(testContactUsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testContactUsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactUsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContactUsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testContactUsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingContactUsData() throws Exception {
        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();
        contactUsData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactUsData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactUsData() throws Exception {
        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();
        contactUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactUsData() throws Exception {
        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();
        contactUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactUsData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactUsDataWithPatch() throws Exception {
        // Initialize the database
        contactUsDataRepository.saveAndFlush(contactUsData);

        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();

        // Update the contactUsData using partial update
        ContactUsData partialUpdatedContactUsData = new ContactUsData();
        partialUpdatedContactUsData.setId(contactUsData.getId());

        partialUpdatedContactUsData
            .lang(UPDATED_LANG)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restContactUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactUsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactUsData))
            )
            .andExpect(status().isOk());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
        ContactUsData testContactUsData = contactUsDataList.get(contactUsDataList.size() - 1);
        assertThat(testContactUsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testContactUsData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContactUsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContactUsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testContactUsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateContactUsDataWithPatch() throws Exception {
        // Initialize the database
        contactUsDataRepository.saveAndFlush(contactUsData);

        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();

        // Update the contactUsData using partial update
        ContactUsData partialUpdatedContactUsData = new ContactUsData();
        partialUpdatedContactUsData.setId(contactUsData.getId());

        partialUpdatedContactUsData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restContactUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactUsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactUsData))
            )
            .andExpect(status().isOk());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
        ContactUsData testContactUsData = contactUsDataList.get(contactUsDataList.size() - 1);
        assertThat(testContactUsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testContactUsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContactUsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testContactUsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testContactUsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingContactUsData() throws Exception {
        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();
        contactUsData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactUsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactUsData() throws Exception {
        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();
        contactUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactUsData() throws Exception {
        int databaseSizeBeforeUpdate = contactUsDataRepository.findAll().size();
        contactUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contactUsData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactUsData in the database
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactUsData() throws Exception {
        // Initialize the database
        contactUsDataRepository.saveAndFlush(contactUsData);

        int databaseSizeBeforeDelete = contactUsDataRepository.findAll().size();

        // Delete the contactUsData
        restContactUsDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactUsData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactUsData> contactUsDataList = contactUsDataRepository.findAll();
        assertThat(contactUsDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
