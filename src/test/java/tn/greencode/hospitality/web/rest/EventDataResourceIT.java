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
import tn.greencode.hospitality.domain.EventData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.EventDataRepository;

/**
 * Integration tests for the {@link EventDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/event-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventDataRepository eventDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventDataMockMvc;

    private EventData eventData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventData createEntity(EntityManager em) {
        EventData eventData = new EventData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return eventData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventData createUpdatedEntity(EntityManager em) {
        EventData eventData = new EventData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return eventData;
    }

    @BeforeEach
    public void initTest() {
        eventData = createEntity(em);
    }

    @Test
    @Transactional
    void createEventData() throws Exception {
        int databaseSizeBeforeCreate = eventDataRepository.findAll().size();
        // Create the EventData
        restEventDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventData)))
            .andExpect(status().isCreated());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeCreate + 1);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testEventData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEventData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEventData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createEventDataWithExistingId() throws Exception {
        // Create the EventData with an existing ID
        eventData.setId(1L);

        int databaseSizeBeforeCreate = eventDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventData)))
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        // Get all the eventDataList
        restEventDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        // Get the eventData
        restEventDataMockMvc
            .perform(get(ENTITY_API_URL_ID, eventData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingEventData() throws Exception {
        // Get the eventData
        restEventDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();

        // Update the eventData
        EventData updatedEventData = eventDataRepository.findById(eventData.getId()).get();
        // Disconnect from session so that the updates on updatedEventData are not directly saved in db
        em.detach(updatedEventData);
        updatedEventData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restEventDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventData))
            )
            .andExpect(status().isOk());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testEventData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEventData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEventData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventDataWithPatch() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();

        // Update the eventData using partial update
        EventData partialUpdatedEventData = new EventData();
        partialUpdatedEventData.setId(eventData.getId());

        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventData))
            )
            .andExpect(status().isOk());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testEventData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEventData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testEventData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testEventData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateEventDataWithPatch() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();

        // Update the eventData using partial update
        EventData partialUpdatedEventData = new EventData();
        partialUpdatedEventData.setId(eventData.getId());

        partialUpdatedEventData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventData))
            )
            .andExpect(status().isOk());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
        EventData testEventData = eventDataList.get(eventDataList.size() - 1);
        assertThat(testEventData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testEventData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEventData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testEventData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testEventData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventData() throws Exception {
        int databaseSizeBeforeUpdate = eventDataRepository.findAll().size();
        eventData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventData in the database
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventData() throws Exception {
        // Initialize the database
        eventDataRepository.saveAndFlush(eventData);

        int databaseSizeBeforeDelete = eventDataRepository.findAll().size();

        // Delete the eventData
        restEventDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventData> eventDataList = eventDataRepository.findAll();
        assertThat(eventDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
