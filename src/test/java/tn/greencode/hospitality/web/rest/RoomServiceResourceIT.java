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
import tn.greencode.hospitality.domain.RoomService;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.RoomServiceRepository;

/**
 * Integration tests for the {@link RoomServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomServiceResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/room-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomServiceRepository roomServiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomServiceMockMvc;

    private RoomService roomService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomService createEntity(EntityManager em) {
        RoomService roomService = new RoomService()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return roomService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomService createUpdatedEntity(EntityManager em) {
        RoomService roomService = new RoomService()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return roomService;
    }

    @BeforeEach
    public void initTest() {
        roomService = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomService() throws Exception {
        int databaseSizeBeforeCreate = roomServiceRepository.findAll().size();
        // Create the RoomService
        restRoomServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomService)))
            .andExpect(status().isCreated());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeCreate + 1);
        RoomService testRoomService = roomServiceList.get(roomServiceList.size() - 1);
        assertThat(testRoomService.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRoomService.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testRoomService.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testRoomService.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createRoomServiceWithExistingId() throws Exception {
        // Create the RoomService with an existing ID
        roomService.setId(1L);

        int databaseSizeBeforeCreate = roomServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomService)))
            .andExpect(status().isBadRequest());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoomServices() throws Exception {
        // Initialize the database
        roomServiceRepository.saveAndFlush(roomService);

        // Get all the roomServiceList
        restRoomServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomService.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getRoomService() throws Exception {
        // Initialize the database
        roomServiceRepository.saveAndFlush(roomService);

        // Get the roomService
        restRoomServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, roomService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomService.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoomService() throws Exception {
        // Get the roomService
        restRoomServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoomService() throws Exception {
        // Initialize the database
        roomServiceRepository.saveAndFlush(roomService);

        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();

        // Update the roomService
        RoomService updatedRoomService = roomServiceRepository.findById(roomService.getId()).get();
        // Disconnect from session so that the updates on updatedRoomService are not directly saved in db
        em.detach(updatedRoomService);
        updatedRoomService
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restRoomServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoomService))
            )
            .andExpect(status().isOk());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
        RoomService testRoomService = roomServiceList.get(roomServiceList.size() - 1);
        assertThat(testRoomService.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRoomService.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testRoomService.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testRoomService.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingRoomService() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();
        roomService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomService))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomService() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();
        roomService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomService))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomService() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();
        roomService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomService)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomServiceWithPatch() throws Exception {
        // Initialize the database
        roomServiceRepository.saveAndFlush(roomService);

        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();

        // Update the roomService using partial update
        RoomService partialUpdatedRoomService = new RoomService();
        partialUpdatedRoomService.setId(roomService.getId());

        partialUpdatedRoomService.publish(UPDATED_PUBLISH).contentPosition(UPDATED_CONTENT_POSITION).imagePosition(UPDATED_IMAGE_POSITION);

        restRoomServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomService))
            )
            .andExpect(status().isOk());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
        RoomService testRoomService = roomServiceList.get(roomServiceList.size() - 1);
        assertThat(testRoomService.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRoomService.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testRoomService.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testRoomService.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateRoomServiceWithPatch() throws Exception {
        // Initialize the database
        roomServiceRepository.saveAndFlush(roomService);

        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();

        // Update the roomService using partial update
        RoomService partialUpdatedRoomService = new RoomService();
        partialUpdatedRoomService.setId(roomService.getId());

        partialUpdatedRoomService
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restRoomServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomService))
            )
            .andExpect(status().isOk());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
        RoomService testRoomService = roomServiceList.get(roomServiceList.size() - 1);
        assertThat(testRoomService.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRoomService.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testRoomService.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testRoomService.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingRoomService() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();
        roomService.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomService))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomService() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();
        roomService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomService))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomService() throws Exception {
        int databaseSizeBeforeUpdate = roomServiceRepository.findAll().size();
        roomService.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomServiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomService in the database
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomService() throws Exception {
        // Initialize the database
        roomServiceRepository.saveAndFlush(roomService);

        int databaseSizeBeforeDelete = roomServiceRepository.findAll().size();

        // Delete the roomService
        restRoomServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomService> roomServiceList = roomServiceRepository.findAll();
        assertThat(roomServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
