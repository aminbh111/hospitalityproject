package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.EventData;

/**
 * Service Interface for managing {@link EventData}.
 */
public interface EventDataService {
    /**
     * Save a eventData.
     *
     * @param eventData the entity to save.
     * @return the persisted entity.
     */
    EventData save(EventData eventData);

    /**
     * Updates a eventData.
     *
     * @param eventData the entity to update.
     * @return the persisted entity.
     */
    EventData update(EventData eventData);

    /**
     * Partially updates a eventData.
     *
     * @param eventData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventData> partialUpdate(EventData eventData);

    /**
     * Get all the eventData.
     *
     * @return the list of entities.
     */
    List<EventData> findAll();

    /**
     * Get the "id" eventData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventData> findOne(Long id);

    /**
     * Delete the "id" eventData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
