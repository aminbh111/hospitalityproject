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
import tn.greencode.hospitality.domain.Meeting;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.MeetingRepository;

/**
 * Integration tests for the {@link MeetingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeetingResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/meetings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeetingMockMvc;

    private Meeting meeting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createEntity(EntityManager em) {
        Meeting meeting = new Meeting()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return meeting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createUpdatedEntity(EntityManager em) {
        Meeting meeting = new Meeting()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return meeting;
    }

    @BeforeEach
    public void initTest() {
        meeting = createEntity(em);
    }

    @Test
    @Transactional
    void createMeeting() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();
        // Create the Meeting
        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate + 1);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMeeting.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testMeeting.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testMeeting.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createMeetingWithExistingId() throws Exception {
        // Create the Meeting with an existing ID
        meeting.setId(1L);

        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeetings() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetingList
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get the meeting
        restMeetingMockMvc
            .perform(get(ENTITY_API_URL_ID, meeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meeting.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMeeting() throws Exception {
        // Get the meeting
        restMeetingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting
        Meeting updatedMeeting = meetingRepository.findById(meeting.getId()).get();
        // Disconnect from session so that the updates on updatedMeeting are not directly saved in db
        em.detach(updatedMeeting);
        updatedMeeting
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeeting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMeeting.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testMeeting.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testMeeting.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meeting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeetingWithPatch() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting using partial update
        Meeting partialUpdatedMeeting = new Meeting();
        partialUpdatedMeeting.setId(meeting.getId());

        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMeeting.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testMeeting.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testMeeting.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateMeetingWithPatch() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting using partial update
        Meeting partialUpdatedMeeting = new Meeting();
        partialUpdatedMeeting.setId(meeting.getId());

        partialUpdatedMeeting
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMeeting))
            )
            .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetingList.get(meetingList.size() - 1);
        assertThat(testMeeting.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMeeting.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testMeeting.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testMeeting.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meeting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(meeting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeeting() throws Exception {
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();
        meeting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(meeting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Meeting in the database
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        int databaseSizeBeforeDelete = meetingRepository.findAll().size();

        // Delete the meeting
        restMeetingMockMvc
            .perform(delete(ENTITY_API_URL_ID, meeting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Meeting> meetingList = meetingRepository.findAll();
        assertThat(meetingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
