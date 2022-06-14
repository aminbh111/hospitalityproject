package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.Offer;

/**
 * Service Interface for managing {@link Offer}.
 */
public interface OfferService {
    /**
     * Save a offer.
     *
     * @param offer the entity to save.
     * @return the persisted entity.
     */
    Offer save(Offer offer);

    /**
     * Updates a offer.
     *
     * @param offer the entity to update.
     * @return the persisted entity.
     */
    Offer update(Offer offer);

    /**
     * Partially updates a offer.
     *
     * @param offer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Offer> partialUpdate(Offer offer);

    /**
     * Get all the offers.
     *
     * @return the list of entities.
     */
    List<Offer> findAll();

    /**
     * Get the "id" offer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Offer> findOne(Long id);

    /**
     * Delete the "id" offer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
