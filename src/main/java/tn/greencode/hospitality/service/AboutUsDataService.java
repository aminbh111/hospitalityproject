package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.AboutUsData;

/**
 * Service Interface for managing {@link AboutUsData}.
 */
public interface AboutUsDataService {
    /**
     * Save a aboutUsData.
     *
     * @param aboutUsData the entity to save.
     * @return the persisted entity.
     */
    AboutUsData save(AboutUsData aboutUsData);

    /**
     * Updates a aboutUsData.
     *
     * @param aboutUsData the entity to update.
     * @return the persisted entity.
     */
    AboutUsData update(AboutUsData aboutUsData);

    /**
     * Partially updates a aboutUsData.
     *
     * @param aboutUsData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AboutUsData> partialUpdate(AboutUsData aboutUsData);

    /**
     * Get all the aboutUsData.
     *
     * @return the list of entities.
     */
    List<AboutUsData> findAll();

    /**
     * Get the "id" aboutUsData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AboutUsData> findOne(Long id);

    /**
     * Delete the "id" aboutUsData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
