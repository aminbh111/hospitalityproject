package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.BarsData;

/**
 * Service Interface for managing {@link BarsData}.
 */
public interface BarsDataService {
    /**
     * Save a barsData.
     *
     * @param barsData the entity to save.
     * @return the persisted entity.
     */
    BarsData save(BarsData barsData);

    /**
     * Updates a barsData.
     *
     * @param barsData the entity to update.
     * @return the persisted entity.
     */
    BarsData update(BarsData barsData);

    /**
     * Partially updates a barsData.
     *
     * @param barsData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BarsData> partialUpdate(BarsData barsData);

    /**
     * Get all the barsData.
     *
     * @return the list of entities.
     */
    List<BarsData> findAll();

    /**
     * Get the "id" barsData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BarsData> findOne(Long id);

    /**
     * Delete the "id" barsData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
