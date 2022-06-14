package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.RoomService;

/**
 * Service Interface for managing {@link RoomService}.
 */
public interface RoomServiceService {
    /**
     * Save a roomService.
     *
     * @param roomService the entity to save.
     * @return the persisted entity.
     */
    RoomService save(RoomService roomService);

    /**
     * Updates a roomService.
     *
     * @param roomService the entity to update.
     * @return the persisted entity.
     */
    RoomService update(RoomService roomService);

    /**
     * Partially updates a roomService.
     *
     * @param roomService the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RoomService> partialUpdate(RoomService roomService);

    /**
     * Get all the roomServices.
     *
     * @return the list of entities.
     */
    List<RoomService> findAll();

    /**
     * Get the "id" roomService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomService> findOne(Long id);

    /**
     * Delete the "id" roomService.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
