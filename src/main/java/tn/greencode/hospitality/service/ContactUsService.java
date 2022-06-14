package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.ContactUs;

/**
 * Service Interface for managing {@link ContactUs}.
 */
public interface ContactUsService {
    /**
     * Save a contactUs.
     *
     * @param contactUs the entity to save.
     * @return the persisted entity.
     */
    ContactUs save(ContactUs contactUs);

    /**
     * Updates a contactUs.
     *
     * @param contactUs the entity to update.
     * @return the persisted entity.
     */
    ContactUs update(ContactUs contactUs);

    /**
     * Partially updates a contactUs.
     *
     * @param contactUs the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactUs> partialUpdate(ContactUs contactUs);

    /**
     * Get all the contactuses.
     *
     * @return the list of entities.
     */
    List<ContactUs> findAll();

    /**
     * Get the "id" contactUs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactUs> findOne(Long id);

    /**
     * Delete the "id" contactUs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
