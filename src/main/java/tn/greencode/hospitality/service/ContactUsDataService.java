package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.ContactUsData;

/**
 * Service Interface for managing {@link ContactUsData}.
 */
public interface ContactUsDataService {
    /**
     * Save a contactUsData.
     *
     * @param contactUsData the entity to save.
     * @return the persisted entity.
     */
    ContactUsData save(ContactUsData contactUsData);

    /**
     * Updates a contactUsData.
     *
     * @param contactUsData the entity to update.
     * @return the persisted entity.
     */
    ContactUsData update(ContactUsData contactUsData);

    /**
     * Partially updates a contactUsData.
     *
     * @param contactUsData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactUsData> partialUpdate(ContactUsData contactUsData);

    /**
     * Get all the contactUsData.
     *
     * @return the list of entities.
     */
    List<ContactUsData> findAll();

    /**
     * Get the "id" contactUsData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactUsData> findOne(Long id);

    /**
     * Delete the "id" contactUsData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
