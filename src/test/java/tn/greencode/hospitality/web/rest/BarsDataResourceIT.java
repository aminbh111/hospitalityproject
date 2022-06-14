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
import tn.greencode.hospitality.domain.BarsData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.BarsDataRepository;

/**
 * Integration tests for the {@link BarsDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BarsDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/bars-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BarsDataRepository barsDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBarsDataMockMvc;

    private BarsData barsData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BarsData createEntity(EntityManager em) {
        BarsData barsData = new BarsData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return barsData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BarsData createUpdatedEntity(EntityManager em) {
        BarsData barsData = new BarsData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return barsData;
    }

    @BeforeEach
    public void initTest() {
        barsData = createEntity(em);
    }

    @Test
    @Transactional
    void createBarsData() throws Exception {
        int databaseSizeBeforeCreate = barsDataRepository.findAll().size();
        // Create the BarsData
        restBarsDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(barsData)))
            .andExpect(status().isCreated());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeCreate + 1);
        BarsData testBarsData = barsDataList.get(barsDataList.size() - 1);
        assertThat(testBarsData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testBarsData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBarsData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBarsData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBarsData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createBarsDataWithExistingId() throws Exception {
        // Create the BarsData with an existing ID
        barsData.setId(1L);

        int databaseSizeBeforeCreate = barsDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarsDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(barsData)))
            .andExpect(status().isBadRequest());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBarsData() throws Exception {
        // Initialize the database
        barsDataRepository.saveAndFlush(barsData);

        // Get all the barsDataList
        restBarsDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barsData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getBarsData() throws Exception {
        // Initialize the database
        barsDataRepository.saveAndFlush(barsData);

        // Get the barsData
        restBarsDataMockMvc
            .perform(get(ENTITY_API_URL_ID, barsData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(barsData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingBarsData() throws Exception {
        // Get the barsData
        restBarsDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBarsData() throws Exception {
        // Initialize the database
        barsDataRepository.saveAndFlush(barsData);

        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();

        // Update the barsData
        BarsData updatedBarsData = barsDataRepository.findById(barsData.getId()).get();
        // Disconnect from session so that the updates on updatedBarsData are not directly saved in db
        em.detach(updatedBarsData);
        updatedBarsData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restBarsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBarsData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBarsData))
            )
            .andExpect(status().isOk());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
        BarsData testBarsData = barsDataList.get(barsDataList.size() - 1);
        assertThat(testBarsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testBarsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBarsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBarsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBarsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBarsData() throws Exception {
        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();
        barsData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, barsData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(barsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBarsData() throws Exception {
        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();
        barsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(barsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBarsData() throws Exception {
        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();
        barsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(barsData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBarsDataWithPatch() throws Exception {
        // Initialize the database
        barsDataRepository.saveAndFlush(barsData);

        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();

        // Update the barsData using partial update
        BarsData partialUpdatedBarsData = new BarsData();
        partialUpdatedBarsData.setId(barsData.getId());

        partialUpdatedBarsData
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restBarsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBarsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBarsData))
            )
            .andExpect(status().isOk());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
        BarsData testBarsData = barsDataList.get(barsDataList.size() - 1);
        assertThat(testBarsData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testBarsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBarsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBarsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBarsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBarsDataWithPatch() throws Exception {
        // Initialize the database
        barsDataRepository.saveAndFlush(barsData);

        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();

        // Update the barsData using partial update
        BarsData partialUpdatedBarsData = new BarsData();
        partialUpdatedBarsData.setId(barsData.getId());

        partialUpdatedBarsData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restBarsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBarsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBarsData))
            )
            .andExpect(status().isOk());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
        BarsData testBarsData = barsDataList.get(barsDataList.size() - 1);
        assertThat(testBarsData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testBarsData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBarsData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBarsData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBarsData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBarsData() throws Exception {
        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();
        barsData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, barsData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(barsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBarsData() throws Exception {
        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();
        barsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(barsData))
            )
            .andExpect(status().isBadRequest());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBarsData() throws Exception {
        int databaseSizeBeforeUpdate = barsDataRepository.findAll().size();
        barsData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBarsDataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(barsData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BarsData in the database
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBarsData() throws Exception {
        // Initialize the database
        barsDataRepository.saveAndFlush(barsData);

        int databaseSizeBeforeDelete = barsDataRepository.findAll().size();

        // Delete the barsData
        restBarsDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, barsData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BarsData> barsDataList = barsDataRepository.findAll();
        assertThat(barsDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
