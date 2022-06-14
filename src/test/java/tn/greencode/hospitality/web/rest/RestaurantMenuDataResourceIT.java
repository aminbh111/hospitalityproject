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
import tn.greencode.hospitality.domain.RestaurantMenuData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.RestaurantMenuDataRepository;

/**
 * Integration tests for the {@link RestaurantMenuDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurantMenuDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/restaurant-menu-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantMenuDataRepository restaurantMenuDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantMenuDataMockMvc;

    private RestaurantMenuData restaurantMenuData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantMenuData createEntity(EntityManager em) {
        RestaurantMenuData restaurantMenuData = new RestaurantMenuData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return restaurantMenuData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantMenuData createUpdatedEntity(EntityManager em) {
        RestaurantMenuData restaurantMenuData = new RestaurantMenuData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return restaurantMenuData;
    }

    @BeforeEach
    public void initTest() {
        restaurantMenuData = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurantMenuData() throws Exception {
        int databaseSizeBeforeCreate = restaurantMenuDataRepository.findAll().size();
        // Create the RestaurantMenuData
        restRestaurantMenuDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isCreated());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantMenuData testRestaurantMenuData = restaurantMenuDataList.get(restaurantMenuDataList.size() - 1);
        assertThat(testRestaurantMenuData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testRestaurantMenuData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRestaurantMenuData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testRestaurantMenuData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testRestaurantMenuData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createRestaurantMenuDataWithExistingId() throws Exception {
        // Create the RestaurantMenuData with an existing ID
        restaurantMenuData.setId(1L);

        int databaseSizeBeforeCreate = restaurantMenuDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMenuDataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaurantMenuData() throws Exception {
        // Initialize the database
        restaurantMenuDataRepository.saveAndFlush(restaurantMenuData);

        // Get all the restaurantMenuDataList
        restRestaurantMenuDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantMenuData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getRestaurantMenuData() throws Exception {
        // Initialize the database
        restaurantMenuDataRepository.saveAndFlush(restaurantMenuData);

        // Get the restaurantMenuData
        restRestaurantMenuDataMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurantMenuData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantMenuData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingRestaurantMenuData() throws Exception {
        // Get the restaurantMenuData
        restRestaurantMenuDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurantMenuData() throws Exception {
        // Initialize the database
        restaurantMenuDataRepository.saveAndFlush(restaurantMenuData);

        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();

        // Update the restaurantMenuData
        RestaurantMenuData updatedRestaurantMenuData = restaurantMenuDataRepository.findById(restaurantMenuData.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantMenuData are not directly saved in db
        em.detach(updatedRestaurantMenuData);
        updatedRestaurantMenuData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRestaurantMenuDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurantMenuData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRestaurantMenuData))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenuData testRestaurantMenuData = restaurantMenuDataList.get(restaurantMenuDataList.size() - 1);
        assertThat(testRestaurantMenuData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testRestaurantMenuData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRestaurantMenuData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRestaurantMenuData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRestaurantMenuData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRestaurantMenuData() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();
        restaurantMenuData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantMenuData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurantMenuData() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();
        restaurantMenuData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurantMenuData() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();
        restaurantMenuData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuDataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantMenuDataWithPatch() throws Exception {
        // Initialize the database
        restaurantMenuDataRepository.saveAndFlush(restaurantMenuData);

        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();

        // Update the restaurantMenuData using partial update
        RestaurantMenuData partialUpdatedRestaurantMenuData = new RestaurantMenuData();
        partialUpdatedRestaurantMenuData.setId(restaurantMenuData.getId());

        partialUpdatedRestaurantMenuData.title(UPDATED_TITLE);

        restRestaurantMenuDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantMenuData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantMenuData))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenuData testRestaurantMenuData = restaurantMenuDataList.get(restaurantMenuDataList.size() - 1);
        assertThat(testRestaurantMenuData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testRestaurantMenuData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRestaurantMenuData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testRestaurantMenuData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testRestaurantMenuData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantMenuDataWithPatch() throws Exception {
        // Initialize the database
        restaurantMenuDataRepository.saveAndFlush(restaurantMenuData);

        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();

        // Update the restaurantMenuData using partial update
        RestaurantMenuData partialUpdatedRestaurantMenuData = new RestaurantMenuData();
        partialUpdatedRestaurantMenuData.setId(restaurantMenuData.getId());

        partialUpdatedRestaurantMenuData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restRestaurantMenuDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantMenuData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantMenuData))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenuData testRestaurantMenuData = restaurantMenuDataList.get(restaurantMenuDataList.size() - 1);
        assertThat(testRestaurantMenuData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testRestaurantMenuData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRestaurantMenuData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testRestaurantMenuData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testRestaurantMenuData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurantMenuData() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();
        restaurantMenuData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantMenuData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurantMenuData() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();
        restaurantMenuData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurantMenuData() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuDataRepository.findAll().size();
        restaurantMenuData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenuData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantMenuData in the database
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurantMenuData() throws Exception {
        // Initialize the database
        restaurantMenuDataRepository.saveAndFlush(restaurantMenuData);

        int databaseSizeBeforeDelete = restaurantMenuDataRepository.findAll().size();

        // Delete the restaurantMenuData
        restRestaurantMenuDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurantMenuData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RestaurantMenuData> restaurantMenuDataList = restaurantMenuDataRepository.findAll();
        assertThat(restaurantMenuDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
