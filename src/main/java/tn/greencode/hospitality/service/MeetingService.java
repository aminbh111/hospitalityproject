package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.Meeting;

/**
 * Service Interface for managing {@link Meeting}.
 */
public interface MeetingService {
    /**
     * Save a meeting.
     *
     * @param meeting the entity to save.
     * @return the persisted entity.
     */
    Meeting save(Meeting meeting);

    /**
     * Updates a meeting.
     *
     * @param meeting the entity to update.
     * @return the persisted entity.
     */
    Meeting update(Meeting meeting);

    /**
     * Partially updates a meeting.
     *
     * @param meeting the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Meeting> partialUpdate(Meeting meeting);

    /**
     * Get all the meetings.
     *
     * @return the list of entities.
     */
    List<Meeting> findAll();

    /**
     * Get the "id" meeting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Meeting> findOne(Long id);

    /**
     * Delete the "id" meeting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
