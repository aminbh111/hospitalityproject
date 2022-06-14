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
import tn.greencode.hospitality.domain.MeetingData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.MeetingDataRepository;

/**
 * Integration tests for the {@link MeetingDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeetingDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/meeting-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeetingDataRepository meetingDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeetingDataMockMvc;

    private MeetingData meetingData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeetingData createEntity(EntityManager em) {
        MeetingData meetingData = new MeetingData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return meetingData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeetingData createUpdatedEntity(EntityManager em) {
        MeetingData meetingData = new MeetingData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return meetingData;
    }

    @BeforeEach
    public void initTest() {
        meetingData = createEntity(em);
    }

    @Test
    @Transactional
    void createMeetingData() throws Exception {
        int databaseSizeBeforeCreate = meetingDataRepository.findAll().size();
        // Create the MeetingData
        restMeetingDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meetingData)))
            .andExpect(status().isCreated());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeCreate + 1);
        MeetingData testMeetingData = meetingDataList.get(meetingDataList.size() - 1);
        assertThat(testMeetingData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testMeetingData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMeetingData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMeetingData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMeetingData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createMeetingDataWithExistingId() throws Exception {
        // Create the MeetingData with an existing ID
        meetingData.setId(1L);

        int databaseSizeBeforeCreate = meetingDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meetingData)))
            .andExpect(status().isBadRequest());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeetingData() throws Exception {
        // Initialize the database
        meetingDataRepository.saveAndFlush(meetingData);

        // Get all the meetingDataList
        restMeetingDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetingData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getMeetingData() throws Exception {
        // Initialize the database
        meetingDataRepository.saveAndFlush(meetingData);

        // Get the meetingData
        restMeetingDataMockMvc
            .perform(get(ENTITY_API_URL_ID, meetingData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meetingData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingMeetingData() throws Exception {
        // Get the meetingData
        restMeetingDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeetingData() throws Exception {
        // Initialize the database
        meetingDataRepository.saveAndFlush(meetingData);

        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();

        // Update the meetingData
        MeetingData updatedMeetingData = meetingDataRepository.findById(meetingData.getId()).get();
        // Disconnect from session so that the updates on updatedMeetingData are not directly saved in db
        em.detach(updatedMeetingData);
        updatedMeetingData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restMeetingDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeetingData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeetingData))
            )
            .andExpect(status().isOk());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
        MeetingData testMeetingData = meetingDataList.get(meetingDataList.size() - 1);
        assertThat(testMeetingData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testMeetingData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMeetingData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMeetingData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMeetingData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingMeetingData() throws Exception {
        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();
        meetingData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meetingData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meetingData))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeetingData() throws Exception {
        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();
        meetingData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meetingData))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeetingData() throws Exception {
        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();
        meetingData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meetingData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeetingDataWithPatch() throws Exception {
        // Initialize the database
        meetingDataRepository.saveAndFlush(meetingData);

        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();

        // Update the meetingData using partial update
        MeetingData partialUpdatedMeetingData = new MeetingData();
        partialUpdatedMeetingData.setId(meetingData.getId());

        partialUpdatedMeetingData.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restMeetingDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeetingData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeetingData))
            )
            .andExpect(status().isOk());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
        MeetingData testMeetingData = meetingDataList.get(meetingDataList.size() - 1);
        assertThat(testMeetingData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testMeetingData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMeetingData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMeetingData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMeetingData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMeetingDataWithPatch() throws Exception {
        // Initialize the database
        meetingDataRepository.saveAndFlush(meetingData);

        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();

        // Update the meetingData using partial update
        MeetingData partialUpdatedMeetingData = new MeetingData();
        partialUpdatedMeetingData.setId(meetingData.getId());

        partialUpdatedMeetingData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restMeetingDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeetingData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeetingData))
            )
            .andExpect(status().isOk());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
        MeetingData testMeetingData = meetingDataList.get(meetingDataList.size() - 1);
        assertThat(testMeetingData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testMeetingData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMeetingData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMeetingData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMeetingData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMeetingData() throws Exception {
        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();
        meetingData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meetingData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meetingData))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeetingData() throws Exception {
        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();
        meetingData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meetingData))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeetingData() throws Exception {
        int databaseSizeBeforeUpdate = meetingDataRepository.findAll().size();
        meetingData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meetingData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeetingData in the database
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeetingData() throws Exception {
        // Initialize the database
        meetingDataRepository.saveAndFlush(meetingData);

        int databaseSizeBeforeDelete = meetingDataRepository.findAll().size();

        // Delete the meetingData
        restMeetingDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, meetingData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MeetingData> meetingDataList = meetingDataRepository.findAll();
        assertThat(meetingDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
