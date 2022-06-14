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
import tn.greencode.hospitality.domain.ConciergeData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.ConciergeDataRepository;

/**
 * Integration tests for the {@link ConciergeDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConciergeDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/concierge-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConciergeDataRepository conciergeDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConciergeDataMockMvc;

    private ConciergeData conciergeData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConciergeData createEntity(EntityManager em) {
        ConciergeData conciergeData = new ConciergeData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return conciergeData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConciergeData createUpdatedEntity(EntityManager em) {
        ConciergeData conciergeData = new ConciergeData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return conciergeData;
    }

    @BeforeEach
    public void initTest() {
        conciergeData = createEntity(em);
    }

    @Test
    @Transactional
    void createConciergeData() throws Exception {
        int databaseSizeBeforeCreate = conciergeDataRepository.findAll().size();
        // Create the ConciergeData
        restConciergeDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conciergeData)))
            .andExpect(status().isCreated());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeCreate + 1);
        ConciergeData testConciergeData = conciergeDataList.get(conciergeDataList.size() - 1);
        assertThat(testConciergeData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testConciergeData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testConciergeData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testConciergeData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testConciergeData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createConciergeDataWithExistingId() throws Exception {
        // Create the ConciergeData with an existing ID
        conciergeData.setId(1L);

        int databaseSizeBeforeCreate = conciergeDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConciergeDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conciergeData)))
            .andExpect(status().isBadRequest());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConciergeData() throws Exception {
        // Initialize the database
        conciergeDataRepository.saveAndFlush(conciergeData);

        // Get all the conciergeDataList
        restConciergeDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conciergeData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getConciergeData() throws Exception {
        // Initialize the database
        conciergeDataRepository.saveAndFlush(conciergeData);

        // Get the conciergeData
        restConciergeDataMockMvc
            .perform(get(ENTITY_API_URL_ID, conciergeData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conciergeData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingConciergeData() throws Exception {
        // Get the conciergeData
        restConciergeDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConciergeData() throws Exception {
        // Initialize the database
        conciergeDataRepository.saveAndFlush(conciergeData);

        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();

        // Update the conciergeData
        ConciergeData updatedConciergeData = conciergeDataRepository.findById(conciergeData.getId()).get();
        // Disconnect from session so that the updates on updatedConciergeData are not directly saved in db
        em.detach(updatedConciergeData);
        updatedConciergeData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restConciergeDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConciergeData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConciergeData))
            )
            .andExpect(status().isOk());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
        ConciergeData testConciergeData = conciergeDataList.get(conciergeDataList.size() - 1);
        assertThat(testConciergeData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testConciergeData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testConciergeData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testConciergeData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testConciergeData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingConciergeData() throws Exception {
        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();
        conciergeData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConciergeDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conciergeData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conciergeData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConciergeData() throws Exception {
        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();
        conciergeData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conciergeData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConciergeData() throws Exception {
        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();
        conciergeData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conciergeData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConciergeDataWithPatch() throws Exception {
        // Initialize the database
        conciergeDataRepository.saveAndFlush(conciergeData);

        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();

        // Update the conciergeData using partial update
        ConciergeData partialUpdatedConciergeData = new ConciergeData();
        partialUpdatedConciergeData.setId(conciergeData.getId());

        partialUpdatedConciergeData.content(UPDATED_CONTENT);

        restConciergeDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConciergeData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConciergeData))
            )
            .andExpect(status().isOk());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
        ConciergeData testConciergeData = conciergeDataList.get(conciergeDataList.size() - 1);
        assertThat(testConciergeData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testConciergeData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testConciergeData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testConciergeData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testConciergeData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateConciergeDataWithPatch() throws Exception {
        // Initialize the database
        conciergeDataRepository.saveAndFlush(conciergeData);

        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();

        // Update the conciergeData using partial update
        ConciergeData partialUpdatedConciergeData = new ConciergeData();
        partialUpdatedConciergeData.setId(conciergeData.getId());

        partialUpdatedConciergeData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restConciergeDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConciergeData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConciergeData))
            )
            .andExpect(status().isOk());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
        ConciergeData testConciergeData = conciergeDataList.get(conciergeDataList.size() - 1);
        assertThat(testConciergeData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testConciergeData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testConciergeData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testConciergeData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testConciergeData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingConciergeData() throws Exception {
        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();
        conciergeData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConciergeDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conciergeData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conciergeData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConciergeData() throws Exception {
        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();
        conciergeData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conciergeData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConciergeData() throws Exception {
        int databaseSizeBeforeUpdate = conciergeDataRepository.findAll().size();
        conciergeData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConciergeDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conciergeData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConciergeData in the database
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConciergeData() throws Exception {
        // Initialize the database
        conciergeDataRepository.saveAndFlush(conciergeData);

        int databaseSizeBeforeDelete = conciergeDataRepository.findAll().size();

        // Delete the conciergeData
        restConciergeDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, conciergeData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConciergeData> conciergeDataList = conciergeDataRepository.findAll();
        assertThat(conciergeDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
