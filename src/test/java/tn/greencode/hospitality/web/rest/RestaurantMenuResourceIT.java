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
import tn.greencode.hospitality.domain.RestaurantMenu;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.domain.enumeration.Position;
import tn.greencode.hospitality.repository.RestaurantMenuRepository;

/**
 * Integration tests for the {@link RestaurantMenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurantMenuResourceIT {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_PUBLISH = false;
    private static final Boolean UPDATED_PUBLISH = true;

    private static final Position DEFAULT_CONTENT_POSITION = Position.LEFT;
    private static final Position UPDATED_CONTENT_POSITION = Position.RIGHT;

    private static final Position DEFAULT_IMAGE_POSITION = Position.LEFT;
    private static final Position UPDATED_IMAGE_POSITION = Position.RIGHT;

    private static final String ENTITY_API_URL = "/api/restaurant-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurantMenuRepository restaurantMenuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantMenuMockMvc;

    private RestaurantMenu restaurantMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantMenu createEntity(EntityManager em) {
        RestaurantMenu restaurantMenu = new RestaurantMenu()
            .date(DEFAULT_DATE)
            .publish(DEFAULT_PUBLISH)
            .contentPosition(DEFAULT_CONTENT_POSITION)
            .imagePosition(DEFAULT_IMAGE_POSITION);
        return restaurantMenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantMenu createUpdatedEntity(EntityManager em) {
        RestaurantMenu restaurantMenu = new RestaurantMenu()
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);
        return restaurantMenu;
    }

    @BeforeEach
    public void initTest() {
        restaurantMenu = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurantMenu() throws Exception {
        int databaseSizeBeforeCreate = restaurantMenuRepository.findAll().size();
        // Create the RestaurantMenu
        restRestaurantMenuMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isCreated());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeCreate + 1);
        RestaurantMenu testRestaurantMenu = restaurantMenuList.get(restaurantMenuList.size() - 1);
        assertThat(testRestaurantMenu.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRestaurantMenu.getPublish()).isEqualTo(DEFAULT_PUBLISH);
        assertThat(testRestaurantMenu.getContentPosition()).isEqualTo(DEFAULT_CONTENT_POSITION);
        assertThat(testRestaurantMenu.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void createRestaurantMenuWithExistingId() throws Exception {
        // Create the RestaurantMenu with an existing ID
        restaurantMenu.setId(1L);

        int databaseSizeBeforeCreate = restaurantMenuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMenuMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaurantMenus() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        // Get all the restaurantMenuList
        restRestaurantMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantMenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].publish").value(hasItem(DEFAULT_PUBLISH.booleanValue())))
            .andExpect(jsonPath("$.[*].contentPosition").value(hasItem(DEFAULT_CONTENT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].imagePosition").value(hasItem(DEFAULT_IMAGE_POSITION.toString())));
    }

    @Test
    @Transactional
    void getRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        // Get the restaurantMenu
        restRestaurantMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurantMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantMenu.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.publish").value(DEFAULT_PUBLISH.booleanValue()))
            .andExpect(jsonPath("$.contentPosition").value(DEFAULT_CONTENT_POSITION.toString()))
            .andExpect(jsonPath("$.imagePosition").value(DEFAULT_IMAGE_POSITION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRestaurantMenu() throws Exception {
        // Get the restaurantMenu
        restRestaurantMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();

        // Update the restaurantMenu
        RestaurantMenu updatedRestaurantMenu = restaurantMenuRepository.findById(restaurantMenu.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurantMenu are not directly saved in db
        em.detach(updatedRestaurantMenu);
        updatedRestaurantMenu
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restRestaurantMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurantMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRestaurantMenu))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenu testRestaurantMenu = restaurantMenuList.get(restaurantMenuList.size() - 1);
        assertThat(testRestaurantMenu.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRestaurantMenu.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testRestaurantMenu.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testRestaurantMenu.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();
        restaurantMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();
        restaurantMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();
        restaurantMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurantMenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantMenuWithPatch() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();

        // Update the restaurantMenu using partial update
        RestaurantMenu partialUpdatedRestaurantMenu = new RestaurantMenu();
        partialUpdatedRestaurantMenu.setId(restaurantMenu.getId());

        partialUpdatedRestaurantMenu.publish(UPDATED_PUBLISH).contentPosition(UPDATED_CONTENT_POSITION);

        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantMenu))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenu testRestaurantMenu = restaurantMenuList.get(restaurantMenuList.size() - 1);
        assertThat(testRestaurantMenu.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRestaurantMenu.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testRestaurantMenu.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testRestaurantMenu.getImagePosition()).isEqualTo(DEFAULT_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void fullUpdateRestaurantMenuWithPatch() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();

        // Update the restaurantMenu using partial update
        RestaurantMenu partialUpdatedRestaurantMenu = new RestaurantMenu();
        partialUpdatedRestaurantMenu.setId(restaurantMenu.getId());

        partialUpdatedRestaurantMenu
            .date(UPDATED_DATE)
            .publish(UPDATED_PUBLISH)
            .contentPosition(UPDATED_CONTENT_POSITION)
            .imagePosition(UPDATED_IMAGE_POSITION);

        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurantMenu))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
        RestaurantMenu testRestaurantMenu = restaurantMenuList.get(restaurantMenuList.size() - 1);
        assertThat(testRestaurantMenu.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRestaurantMenu.getPublish()).isEqualTo(UPDATED_PUBLISH);
        assertThat(testRestaurantMenu.getContentPosition()).isEqualTo(UPDATED_CONTENT_POSITION);
        assertThat(testRestaurantMenu.getImagePosition()).isEqualTo(UPDATED_IMAGE_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();
        restaurantMenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();
        restaurantMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurantMenu() throws Exception {
        int databaseSizeBeforeUpdate = restaurantMenuRepository.findAll().size();
        restaurantMenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaurantMenu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantMenu in the database
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        int databaseSizeBeforeDelete = restaurantMenuRepository.findAll().size();

        // Delete the restaurantMenu
        restRestaurantMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurantMenu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RestaurantMenu> restaurantMenuList = restaurantMenuRepository.findAll();
        assertThat(restaurantMenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
