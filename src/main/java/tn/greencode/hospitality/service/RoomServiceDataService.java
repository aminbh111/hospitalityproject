package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.RoomServiceData;

/**
 * Service Interface for managing {@link RoomServiceData}.
 */
public interface RoomServiceDataService {
    /**
     * Save a roomServiceData.
     *
     * @param roomServiceData the entity to save.
     * @return the persisted entity.
     */
    RoomServiceData save(RoomServiceData roomServiceData);

    /**
     * Updates a roomServiceData.
     *
     * @param roomServiceData the entity to update.
     * @return the persisted entity.
     */
    RoomServiceData update(RoomServiceData roomServiceData);

    /**
     * Partially updates a roomServiceData.
     *
     * @param roomServiceData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoomServiceData> partialUpdate(RoomServiceData roomServiceData);

    /**
     * Get all the roomServiceData.
     *
     * @return the list of entities.
     */
    List<RoomServiceData> findAll();

    /**
     * Get the "id" roomServiceData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomServiceData> findOne(Long id);

    /**
     * Delete the "id" roomServiceData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
