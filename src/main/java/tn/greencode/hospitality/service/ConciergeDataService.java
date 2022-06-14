package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.ConciergeData;

/**
 * Service Interface for managing {@link ConciergeData}.
 */
public interface ConciergeDataService {
    /**
     * Save a conciergeData.
     *
     * @param conciergeData the entity to save.
     * @return the persisted entity.
     */
    ConciergeData save(ConciergeData conciergeData);

    /**
     * Updates a conciergeData.
     *
     * @param conciergeData the entity to update.
     * @return the persisted entity.
     */
    ConciergeData update(ConciergeData conciergeData);

    /**
     * Partially updates a conciergeData.
     *
     * @param conciergeData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConciergeData> partialUpdate(ConciergeData conciergeData);

    /**
     * Get all the conciergeData.
     *
     * @return the list of entities.
     */
    List<ConciergeData> findAll();

    /**
     * Get the "id" conciergeData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConciergeData> findOne(Long id);

    /**
     * Delete the "id" conciergeData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
