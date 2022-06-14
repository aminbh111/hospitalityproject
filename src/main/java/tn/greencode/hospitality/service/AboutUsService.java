package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.AboutUs;

/**
 * Service Interface for managing {@link AboutUs}.
 */
public interface AboutUsService {
    /**
     * Save a aboutUs.
     *
     * @param aboutUs the entity to save.
     * @return the persisted entity.
     */
    AboutUs save(AboutUs aboutUs);

    /**
     * Updates a aboutUs.
     *
     * @param aboutUs the entity to update.
     * @return the persisted entity.
     */
    AboutUs update(AboutUs aboutUs);

    /**
     * Partially updates a aboutUs.
     *
     * @param aboutUs the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AboutUs> partialUpdate(AboutUs aboutUs);

    /**
     * Get all the aboutuses.
     *
     * @return the list of entities.
     */
    List<AboutUs> findAll();

    /**
     * Get the "id" aboutUs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AboutUs> findOne(Long id);

    /**
     * Delete the "id" aboutUs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
