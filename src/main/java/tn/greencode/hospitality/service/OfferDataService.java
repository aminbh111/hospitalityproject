package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.OfferData;

/**
 * Service Interface for managing {@link OfferData}.
 */
public interface OfferDataService {
    /**
     * Save a offerData.
     *
     * @param offerData the entity to save.
     * @return the persisted entity.
     */
    OfferData save(OfferData offerData);

    /**
     * Updates a offerData.
     *
     * @param offerData the entity to update.
     * @return the persisted entity.
     */
    OfferData update(OfferData offerData);

    /**
     * Partially updates a offerData.
     *
     * @param offerData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OfferData> partialUpdate(OfferData offerData);

    /**
     * Get all the offerData.
     *
     * @return the list of entities.
     */
    List<OfferData> findAll();

    /**
     * Get the "id" offerData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OfferData> findOne(Long id);

    /**
     * Delete the "id" offerData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
