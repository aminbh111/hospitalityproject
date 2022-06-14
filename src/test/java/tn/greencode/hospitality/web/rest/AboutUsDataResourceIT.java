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
import tn.greencode.hospitality.domain.AboutUsData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.AboutUsDataRepository;

/**
 * Integration tests for the {@link AboutUsDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AboutUsDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/about-us-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AboutUsDataRepository aboutUsDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAboutUsDataMockMvc;

    private AboutUsData aboutUsData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutUsData createEntity(EntityManager em) {
        AboutUsData aboutUsData = new AboutUsData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return aboutUsData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AboutUsData createUpdatedEntity(EntityManager em) {
        AboutUsData aboutUsData = new AboutUsData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return aboutUsData;
    }

    @BeforeEach
    public void initTest() {
        aboutUsData = createEntity(em);
    }

    @Test
    @Transactional
    void createAboutUsData() throws Exception {
        int databaseSizeBeforeCreate = aboutUsDataRepository.findAll().size();
        // Create the AboutUsData
        restAboutUsDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aboutUsData)))
            .andExpect(status().isCreated());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeCreate + 1);
        AboutUsData testAboutUsData = aboutUsDataList.get(aboutUsDataList.size() - 1);
        assertThat(testAboutUsData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testAboutUsData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAboutUsData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAboutUsData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAboutUsData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAboutUsDataWithExistingId() throws Exception {
        // Create the AboutUsData with an existing ID
        aboutUsData.setId(1L);

        int databaseSizeBeforeCreate = aboutUsDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAboutUsDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aboutUsData)))
            .andExpect(status().isBadRequest());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAboutUsData() throws Exception {
        // Initialize the database
        aboutUsDataRepository.saveAndFlush(aboutUsData);

        // Get all the aboutUsDataList
        restAboutUsDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aboutUsData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getAboutUsData() throws Exception {
        // Initialize the database
        aboutUsDataRepository.saveAndFlush(aboutUsData);

        // Get the aboutUsData
        restAboutUsDataMockMvc
            .perform(get(ENTITY_API_URL_ID, aboutUsData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aboutUsData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingAboutUsData() throws Exception {
        // Get the aboutUsData
        restAboutUsDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAboutUsData() throws Exception {
        // Initialize the database
        aboutUsDataRepository.saveAndFlush(aboutUsData);

        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();

        // Update the aboutUsData
        AboutUsData updatedAboutUsData = aboutUsDataRepository.findById(aboutUsData.getId()).get();
        // Disconnect from session so that the updates on updatedAboutUsData are not directly saved in db
        em.detach(updatedAboutUsData);
        updatedAboutUsData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAboutUsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAboutUsData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAboutUsData))
            )
            .andExpect(status().isOk());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
        AboutUsData testAboutUsData = aboutUsDataList.get(aboutUsDataList.size() - 1);
        assertThat(testAboutUsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testAboutUsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAboutUsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAboutUsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAboutUsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAboutUsData() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();
        aboutUsData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAboutUsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aboutUsData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aboutUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAboutUsData() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();
        aboutUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aboutUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAboutUsData() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();
        aboutUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aboutUsData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAboutUsDataWithPatch() throws Exception {
        // Initialize the database
        aboutUsDataRepository.saveAndFlush(aboutUsData);

        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();

        // Update the aboutUsData using partial update
        AboutUsData partialUpdatedAboutUsData = new AboutUsData();
        partialUpdatedAboutUsData.setId(aboutUsData.getId());

        partialUpdatedAboutUsData.content(UPDATED_CONTENT).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAboutUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAboutUsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAboutUsData))
            )
            .andExpect(status().isOk());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
        AboutUsData testAboutUsData = aboutUsDataList.get(aboutUsDataList.size() - 1);
        assertThat(testAboutUsData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testAboutUsData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAboutUsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAboutUsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAboutUsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAboutUsDataWithPatch() throws Exception {
        // Initialize the database
        aboutUsDataRepository.saveAndFlush(aboutUsData);

        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();

        // Update the aboutUsData using partial update
        AboutUsData partialUpdatedAboutUsData = new AboutUsData();
        partialUpdatedAboutUsData.setId(aboutUsData.getId());

        partialUpdatedAboutUsData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAboutUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAboutUsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAboutUsData))
            )
            .andExpect(status().isOk());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
        AboutUsData testAboutUsData = aboutUsDataList.get(aboutUsDataList.size() - 1);
        assertThat(testAboutUsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testAboutUsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAboutUsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAboutUsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAboutUsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAboutUsData() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();
        aboutUsData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAboutUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aboutUsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aboutUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAboutUsData() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();
        aboutUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aboutUsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAboutUsData() throws Exception {
        int databaseSizeBeforeUpdate = aboutUsDataRepository.findAll().size();
        aboutUsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAboutUsDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aboutUsData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AboutUsData in the database
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAboutUsData() throws Exception {
        // Initialize the database
        aboutUsDataRepository.saveAndFlush(aboutUsData);

        int databaseSizeBeforeDelete = aboutUsDataRepository.findAll().size();

        // Delete the aboutUsData
        restAboutUsDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, aboutUsData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AboutUsData> aboutUsDataList = aboutUsDataRepository.findAll();
        assertThat(aboutUsDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
