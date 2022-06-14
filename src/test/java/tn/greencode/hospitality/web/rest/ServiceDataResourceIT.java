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
import tn.greencode.hospitality.domain.ServiceData;
import tn.greencode.hospitality.domain.enumeration.Language;
import tn.greencode.hospitality.repository.ServiceDataRepository;

/**
 * Integration tests for the {@link ServiceDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceDataResourceIT {

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

    private static final String ENTITY_API_URL = "/api/service-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceDataRepository serviceDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceDataMockMvc;

    private ServiceData serviceData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceData createEntity(EntityManager em) {
        ServiceData serviceData = new ServiceData()
            .lang(DEFAULT_LANG)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return serviceData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceData createUpdatedEntity(EntityManager em) {
        ServiceData serviceData = new ServiceData()
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return serviceData;
    }

    @BeforeEach
    public void initTest() {
        serviceData = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceData() throws Exception {
        int databaseSizeBeforeCreate = serviceDataRepository.findAll().size();
        // Create the ServiceData
        restServiceDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceData)))
            .andExpect(status().isCreated());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceData testServiceData = serviceDataList.get(serviceDataList.size() - 1);
        assertThat(testServiceData.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testServiceData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testServiceData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testServiceData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testServiceData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createServiceDataWithExistingId() throws Exception {
        // Create the ServiceData with an existing ID
        serviceData.setId(1L);

        int databaseSizeBeforeCreate = serviceDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceData)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllServiceData() throws Exception {
        // Initialize the database
        serviceDataRepository.saveAndFlush(serviceData);

        // Get all the serviceDataList
        restServiceDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceData.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getServiceData() throws Exception {
        // Initialize the database
        serviceDataRepository.saveAndFlush(serviceData);

        // Get the serviceData
        restServiceDataMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceData.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingServiceData() throws Exception {
        // Get the serviceData
        restServiceDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServiceData() throws Exception {
        // Initialize the database
        serviceDataRepository.saveAndFlush(serviceData);

        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();

        // Update the serviceData
        ServiceData updatedServiceData = serviceDataRepository.findById(serviceData.getId()).get();
        // Disconnect from session so that the updates on updatedServiceData are not directly saved in db
        em.detach(updatedServiceData);
        updatedServiceData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedServiceData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedServiceData))
            )
            .andExpect(status().isOk());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
        ServiceData testServiceData = serviceDataList.get(serviceDataList.size() - 1);
        assertThat(testServiceData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testServiceData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testServiceData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testServiceData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testServiceData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingServiceData() throws Exception {
        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();
        serviceData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceData() throws Exception {
        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();
        serviceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceData() throws Exception {
        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();
        serviceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceDataWithPatch() throws Exception {
        // Initialize the database
        serviceDataRepository.saveAndFlush(serviceData);

        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();

        // Update the serviceData using partial update
        ServiceData partialUpdatedServiceData = new ServiceData();
        partialUpdatedServiceData.setId(serviceData.getId());

        partialUpdatedServiceData.lang(UPDATED_LANG);

        restServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceData))
            )
            .andExpect(status().isOk());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
        ServiceData testServiceData = serviceDataList.get(serviceDataList.size() - 1);
        assertThat(testServiceData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testServiceData.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testServiceData.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testServiceData.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testServiceData.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateServiceDataWithPatch() throws Exception {
        // Initialize the database
        serviceDataRepository.saveAndFlush(serviceData);

        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();

        // Update the serviceData using partial update
        ServiceData partialUpdatedServiceData = new ServiceData();
        partialUpdatedServiceData.setId(serviceData.getId());

        partialUpdatedServiceData
            .lang(UPDATED_LANG)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceData))
            )
            .andExpect(status().isOk());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
        ServiceData testServiceData = serviceDataList.get(serviceDataList.size() - 1);
        assertThat(testServiceData.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testServiceData.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testServiceData.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testServiceData.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testServiceData.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingServiceData() throws Exception {
        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();
        serviceData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceData() throws Exception {
        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();
        serviceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceData))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceData() throws Exception {
        int databaseSizeBeforeUpdate = serviceDataRepository.findAll().size();
        serviceData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(serviceData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceData in the database
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceData() throws Exception {
        // Initialize the database
        serviceDataRepository.saveAndFlush(serviceData);

        int databaseSizeBeforeDelete = serviceDataRepository.findAll().size();

        // Delete the serviceData
        restServiceDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceData> serviceDataList = serviceDataRepository.findAll();
        assertThat(serviceDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
