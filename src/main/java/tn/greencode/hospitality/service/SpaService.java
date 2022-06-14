package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.Spa;

/**
 * Service Interface for managing {@link Spa}.
 */
public interface SpaService {
    /**
     * Save a spa.
     *
     * @param spa the entity to save.
     * @return the persisted entity.
     */
    Spa save(Spa spa);

    /**
     * Updates a spa.
     *
     * @param spa the entity to update.
     * @return the persisted entity.
     */
    Spa update(Spa spa);

    /**
     * Partially updates a spa.
     *
     * @param spa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Spa> partialUpdate(Spa spa);

    /**
     * Get all the spas.
     *
     * @return the list of entities.
     */
    List<Spa> findAll();

    /**
     * Get the "id" spa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Spa> findOne(Long id);

    /**
     * Delete the "id" spa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
