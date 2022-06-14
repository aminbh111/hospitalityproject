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
import tn.greencode.hospitality.domain.SpaData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.SpaDataRepository;

/**
 * Integration tests for the {@link SpaDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpaDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/spa-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpaDataRepository spaDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpaDataMockMvc;

    private SpaData spaData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpaData createEntity(EntityManager em) {
        SpaData spaData = new SpaData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return spaData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpaData createUpdatedEntity(EntityManager em) {
        SpaData spaData = new SpaData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return spaData;
    }

    @BeforeEach
    public void initTest() {
        spaData = createEntity(em);
    }

    @Test
    @Transactional
    void createSpaData() throws Exception {
        int databaseSizeBeforeCreate = spaDataRepository.findAll().size();
        // Create the SpaData
        restSpaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaData)))
            .andExpect(status().isCreated());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeCreate + 1);
        SpaData testSpaData = spaDataList.get(spaDataList.size() - 1);
        assertThat(testSpaData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testSpaData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSpaData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSpaData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSpaData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSpaDataWithExistingId() throws Exception {
        // Create the SpaData with an existing ID
        spaData.setId(1L);

        int databaseSizeBeforeCreate = spaDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaData)))
            .andExpect(status().isBadRequest());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpaData() throws Exception {
        // Initialize the database
        spaDataRepository.saveAndFlush(spaData);

        // Get all the spaDataList
        restSpaDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spaData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getSpaData() throws Exception {
        // Initialize the database
        spaDataRepository.saveAndFlush(spaData);

        // Get the spaData
        restSpaDataMockMvc
            .perform(get(ENTITY_API_URL_ID, spaData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spaData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingSpaData() throws Exception {
        // Get the spaData
        restSpaDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpaData() throws Exception {
        // Initialize the database
        spaDataRepository.saveAndFlush(spaData);

        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();

        // Update the spaData
        SpaData updatedSpaData = spaDataRepository.findById(spaData.getId()).get();
        // Disconnect from session so that the updates on updatedSpaData are not directly saved in db
        em.detach(updatedSpaData);
        updatedSpaData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restSpaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpaData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpaData))
            )
            .andExpect(status().isOk());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
        SpaData testSpaData = spaDataList.get(spaDataList.size() - 1);
        assertThat(testSpaData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testSpaData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpaData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSpaData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSpaData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSpaData() throws Exception {
        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();
        spaData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spaData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpaData() throws Exception {
        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();
        spaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpaData() throws Exception {
        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();
        spaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spaData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpaDataWithPatch() throws Exception {
        // Initialize the database
        spaDataRepository.saveAndFlush(spaData);

        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();

        // Update the spaData using partial update
        SpaData partialUpdatedSpaData = new SpaData();
        partialUpdatedSpaData.setId(spaData.getId());

        partialUpdatedSpaData.lang(UPDATED_LANG).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restSpaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpaData))
            )
            .andExpect(status().isOk());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
        SpaData testSpaData = spaDataList.get(spaDataList.size() - 1);
        assertThat(testSpaData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testSpaData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSpaData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSpaData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSpaData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSpaDataWithPatch() throws Exception {
        // Initialize the database
        spaDataRepository.saveAndFlush(spaData);

        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();

        // Update the spaData using partial update
        SpaData partialUpdatedSpaData = new SpaData();
        partialUpdatedSpaData.setId(spaData.getId());

        partialUpdatedSpaData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restSpaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpaData))
            )
            .andExpect(status().isOk());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
        SpaData testSpaData = spaDataList.get(spaDataList.size() - 1);
        assertThat(testSpaData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testSpaData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSpaData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSpaData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSpaData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSpaData() throws Exception {
        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();
        spaData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpaData() throws Exception {
        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();
        spaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpaData() throws Exception {
        int databaseSizeBeforeUpdate = spaDataRepository.findAll().size();
        spaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpaDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spaData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpaData in the database
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpaData() throws Exception {
        // Initialize the database
        spaDataRepository.saveAndFlush(spaData);

        int databaseSizeBeforeDelete = spaDataRepository.findAll().size();

        // Delete the spaData
        restSpaDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, spaData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpaData> spaDataList = spaDataRepository.findAll();
        assertThat(spaDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
