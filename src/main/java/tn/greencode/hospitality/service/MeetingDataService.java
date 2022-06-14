package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.MeetingData;

/**
 * Service Interface for managing {@link MeetingData}.
 */
public interface MeetingDataService {
    /**
     * Save a meetingData.
     *
     * @param meetingData the entity to save.
     * @return the persisted entity.
     */
    MeetingData save(MeetingData meetingData);

    /**
     * Updates a meetingData.
     *
     * @param meetingData the entity to update.
     * @return the persisted entity.
     */
    MeetingData update(MeetingData meetingData);

    /**
     * Partially updates a meetingData.
     *
     * @param meetingData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MeetingData> partialUpdate(MeetingData meetingData);

    /**
     * Get all the meetingData.
     *
     * @return the list of entities.
     */
    List<MeetingData> findAll();

    /**
     * Get the "id" meetingData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MeetingData> findOne(Long id);

    /**
     * Delete the "id" meetingData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
