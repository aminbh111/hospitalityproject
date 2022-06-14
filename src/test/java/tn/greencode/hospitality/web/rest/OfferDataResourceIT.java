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
import tn.greencode.hospitality.domain.OfferData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.OfferDataRepository;

/**
 * Integration tests for the {@link OfferDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfferDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/offer-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfferDataRepository offerDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfferDataMockMvc;

    private OfferData offerData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferData createEntity(EntityManager em) {
        OfferData offerData = new OfferData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return offerData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferData createUpdatedEntity(EntityManager em) {
        OfferData offerData = new OfferData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return offerData;
    }

    @BeforeEach
    public void initTest() {
        offerData = createEntity(em);
    }

    @Test
    @Transactional
    void createOfferData() throws Exception {
        int databaseSizeBeforeCreate = offerDataRepository.findAll().size();
        // Create the OfferData
        restOfferDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerData)))
            .andExpect(status().isCreated());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeCreate + 1);
        OfferData testOfferData = offerDataList.get(offerDataList.size() - 1);
        assertThat(testOfferData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testOfferData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOfferData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testOfferData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testOfferData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createOfferDataWithExistingId() throws Exception {
        // Create the OfferData with an existing ID
        offerData.setId(1L);

        int databaseSizeBeforeCreate = offerDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerData)))
            .andExpect(status().isBadRequest());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOfferData() throws Exception {
        // Initialize the database
        offerDataRepository.saveAndFlush(offerData);

        // Get all the offerDataList
        restOfferDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offerData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getOfferData() throws Exception {
        // Initialize the database
        offerDataRepository.saveAndFlush(offerData);

        // Get the offerData
        restOfferDataMockMvc
            .perform(get(ENTITY_API_URL_ID, offerData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offerData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingOfferData() throws Exception {
        // Get the offerData
        restOfferDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOfferData() throws Exception {
        // Initialize the database
        offerDataRepository.saveAndFlush(offerData);

        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();

        // Update the offerData
        OfferData updatedOfferData = offerDataRepository.findById(offerData.getId()).get();
        // Disconnect from session so that the updates on updatedOfferData are not directly saved in db
        em.detach(updatedOfferData);
        updatedOfferData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restOfferDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOfferData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOfferData))
            )
            .andExpect(status().isOk());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
        OfferData testOfferData = offerDataList.get(offerDataList.size() - 1);
        assertThat(testOfferData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testOfferData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOfferData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testOfferData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOfferData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingOfferData() throws Exception {
        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();
        offerData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerData))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOfferData() throws Exception {
        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();
        offerData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerData))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOfferData() throws Exception {
        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();
        offerData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfferDataWithPatch() throws Exception {
        // Initialize the database
        offerDataRepository.saveAndFlush(offerData);

        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();

        // Update the offerData using partial update
        OfferData partialUpdatedOfferData = new OfferData();
        partialUpdatedOfferData.setId(offerData.getId());

        partialUpdatedOfferData.title(UPDATED_TITLE).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restOfferDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfferData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfferData))
            )
            .andExpect(status().isOk());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
        OfferData testOfferData = offerDataList.get(offerDataList.size() - 1);
        assertThat(testOfferData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testOfferData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOfferData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testOfferData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOfferData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateOfferDataWithPatch() throws Exception {
        // Initialize the database
        offerDataRepository.saveAndFlush(offerData);

        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();

        // Update the offerData using partial update
        OfferData partialUpdatedOfferData = new OfferData();
        partialUpdatedOfferData.setId(offerData.getId());

        partialUpdatedOfferData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restOfferDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfferData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfferData))
            )
            .andExpect(status().isOk());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
        OfferData testOfferData = offerDataList.get(offerDataList.size() - 1);
        assertThat(testOfferData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testOfferData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOfferData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testOfferData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testOfferData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingOfferData() throws Exception {
        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();
        offerData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offerData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerData))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOfferData() throws Exception {
        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();
        offerData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerData))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOfferData() throws Exception {
        int databaseSizeBeforeUpdate = offerDataRepository.findAll().size();
        offerData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(offerData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfferData in the database
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOfferData() throws Exception {
        // Initialize the database
        offerDataRepository.saveAndFlush(offerData);

        int databaseSizeBeforeDelete = offerDataRepository.findAll().size();

        // Delete the offerData
        restOfferDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, offerData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OfferData> offerDataList = offerDataRepository.findAll();
        assertThat(offerDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
