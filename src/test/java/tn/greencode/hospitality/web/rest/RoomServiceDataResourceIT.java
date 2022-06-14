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
import tn.greencode.hospitality.domain.RoomServiceData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.RoomServiceDataRepository;

/**
 * Integration tests for the {@link RoomServiceDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomServiceDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/room-service-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomServiceDataRepository roomServiceDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomServiceDataMockMvc;

    private RoomServiceData roomServiceData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomServiceData createEntity(EntityManager em) {
        RoomServiceData roomServiceData = new RoomServiceData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return roomServiceData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomServiceData createUpdatedEntity(EntityManager em) {
        RoomServiceData roomServiceData = new RoomServiceData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return roomServiceData;
    }

    @BeforeEach
    public void initTest() {
        roomServiceData = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomServiceData() throws Exception {
        int databaseSizeBeforeCreate = roomServiceDataRepository.findAll().size();
        // Create the RoomServiceData
        restRoomServiceDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isCreated());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeCreate + 1);
        RoomServiceData testRoomServiceData = roomServiceDataList.get(roomServiceDataList.size() - 1);
        assertThat(testRoomServiceData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testRoomServiceData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRoomServiceData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testRoomServiceData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testRoomServiceData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createRoomServiceDataWithExistingId() throws Exception {
        // Create the RoomServiceData with an existing ID
        roomServiceData.setId(1L);

        int databaseSizeBeforeCreate = roomServiceDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomServiceDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoomServiceData() throws Exception {
        // Initialize the database
        roomServiceDataRepository.saveAndFlush(roomServiceData);

        // Get all the roomServiceDataList
        restRoomServiceDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomServiceData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getRoomServiceData() throws Exception {
        // Initialize the database
        roomServiceDataRepository.saveAndFlush(roomServiceData);

        // Get the roomServiceData
        restRoomServiceDataMockMvc
            .perform(get(ENTITY_API_URL_ID, roomServiceData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomServiceData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingRoomServiceData() throws Exception {
        // Get the roomServiceData
        restRoomServiceDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoomServiceData() throws Exception {
        // Initialize the database
        roomServiceDataRepository.saveAndFlush(roomServiceData);

        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();

        // Update the roomServiceData
        RoomServiceData updatedRoomServiceData = roomServiceDataRepository.findById(roomServiceData.getId()).get();
        // Disconnect from session so that the updates on updatedRoomServiceData are not directly saved in db
        em.detach(updatedRoomServiceData);
        updatedRoomServiceData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRoomServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomServiceData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoomServiceData))
            )
            .andExpect(status().isOk());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
        RoomServiceData testRoomServiceData = roomServiceDataList.get(roomServiceDataList.size() - 1);
        assertThat(testRoomServiceData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testRoomServiceData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRoomServiceData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRoomServiceData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRoomServiceData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRoomServiceData() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();
        roomServiceData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomServiceData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomServiceData() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();
        roomServiceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomServiceData() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();
        roomServiceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomServiceDataWithPatch() throws Exception {
        // Initialize the database
        roomServiceDataRepository.saveAndFlush(roomServiceData);

        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();

        // Update the roomServiceData using partial update
        RoomServiceData partialUpdatedRoomServiceData = new RoomServiceData();
        partialUpdatedRoomServiceData.setId(roomServiceData.getId());

        partialUpdatedRoomServiceData.content(UPDATED_CONTENT).image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRoomServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomServiceData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomServiceData))
            )
            .andExpect(status().isOk());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
        RoomServiceData testRoomServiceData = roomServiceDataList.get(roomServiceDataList.size() - 1);
        assertThat(testRoomServiceData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testRoomServiceData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRoomServiceData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRoomServiceData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRoomServiceData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRoomServiceDataWithPatch() throws Exception {
        // Initialize the database
        roomServiceDataRepository.saveAndFlush(roomServiceData);

        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();

        // Update the roomServiceData using partial update
        RoomServiceData partialUpdatedRoomServiceData = new RoomServiceData();
        partialUpdatedRoomServiceData.setId(roomServiceData.getId());

        partialUpdatedRoomServiceData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRoomServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomServiceData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomServiceData))
            )
            .andExpect(status().isOk());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
        RoomServiceData testRoomServiceData = roomServiceDataList.get(roomServiceDataList.size() - 1);
        assertThat(testRoomServiceData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testRoomServiceData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRoomServiceData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRoomServiceData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRoomServiceData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRoomServiceData() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();
        roomServiceData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomServiceData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomServiceData() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();
        roomServiceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomServiceData() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceDataRepository.findAll().size();
        roomServiceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomServiceData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomServiceData in the database
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomServiceData() throws Exception {
        // Initialize the database
        roomServiceDataRepository.saveAndFlush(roomServiceData);

        int databaseSizeBeforeDelete = roomServiceDataRepository.findAll().size();

        // Delete the roomServiceData
        restRoomServiceDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomServiceData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomServiceData> roomServiceDataList = roomServiceDataRepository.findAll();
        assertThat(roomServiceDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
