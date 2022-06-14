package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.ServiceData;

/**
 * Service Interface for managing {@link ServiceData}.
 */
public interface ServiceDataService {
    /**
     * Save a serviceData.
     *
     * @param serviceData the entity to save.
     * @return the persisted entity.
     */
    ServiceData save(ServiceData serviceData);

    /**
     * Updates a serviceData.
     *
     * @param serviceData the entity to update.
     * @return the persisted entity.
     */
    ServiceData update(ServiceData serviceData);

    /**
     * Partially updates a serviceData.
     *
     * @param serviceData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceData> partialUpdate(ServiceData serviceData);

    /**
     * Get all the serviceData.
     *
     * @return the list of entities.
     */
    List<ServiceData> findAll();

    /**
     * Get the "id" serviceData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceData> findOne(Long id);

    /**
     * Delete the "id" serviceData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
