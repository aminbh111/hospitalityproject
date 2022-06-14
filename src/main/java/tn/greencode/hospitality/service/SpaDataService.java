package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.SpaData;

/**
 * Service Interface for managing {@link SpaData}.
 */
public interface SpaDataService {
    /**
     * Save a spaData.
     *
     * @param spaData the entity to save.
     * @return the persisted entity.
     */
    SpaData save(SpaData spaData);

    /**
     * Updates a spaData.
     *
     * @param spaData the entity to update.
     * @return the persisted entity.
     */
    SpaData update(SpaData spaData);

    /**
     * Partially updates a spaData.
     *
     * @param spaData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpaData> partialUpdate(SpaData spaData);

    /**
     * Get all the spaData.
     *
     * @return the list of entities.
     */
    List<SpaData> findAll();

    /**
     * Get the "id" spaData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpaData> findOne(Long id);

    /**
     * Delete the "id" spaData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
