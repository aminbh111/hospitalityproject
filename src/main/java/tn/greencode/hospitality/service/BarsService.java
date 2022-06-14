package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.Bars;

/**
 * Service Interface for managing {@link Bars}.
 */
public interface BarsService {
    /**
     * Save a bars.
     *
     * @param bars the entity to save.
     * @return the persisted entity.
     */
    Bars save(Bars bars);

    /**
     * Updates a bars.
     *
     * @param bars the entity to update.
     * @return the persisted entity.
     */
    Bars update(Bars bars);

    /**
     * Partially updates a bars.
     *
     * @param bars the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bars> partialUpdate(Bars bars);

    /**
     * Get all the bars.
     *
     * @return the list of entities.
     */
    List<Bars> findAll();

    /**
     * Get the "id" bars.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bars> findOne(Long id);

    /**
     * Delete the "id" bars.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
