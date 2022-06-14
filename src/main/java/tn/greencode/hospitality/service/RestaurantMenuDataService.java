package tn.greencode.hospitality.service;

import java.util.List;
import java.util.Optional;
import tn.greencode.hospitality.domain.RestaurantMenuData;

/**
 * Service Interface for managing {@link RestaurantMenuData}.
 */
public interface RestaurantMenuDataService {
    /**
     * Save a restaurantMenuData.
     *
     * @param restaurantMenuData the entity to save.
     * @return the persisted entity.
     */
    RestaurantMenuData save(RestaurantMenuData restaurantMenuData);

    /**
     * Updates a restaurantMenuData.
     *
     * @param restaurantMenuData the entity to update.
     * @return the persisted entity.
     */
    RestaurantMenuData update(RestaurantMenuData restaurantMenuData);

    /**
     * Partially updates a restaurantMenuData.
     *
     * @param restaurantMenuData the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RestaurantMenuData> partialUpdate(RestaurantMenuData restaurantMenuData);

    /**
     * Get all the restaurantMenuData.
     *
     * @return the list of entities.
     */
    List<RestaurantMenuData> findAll();

    /**
     * Get the "id" restaurantMenuData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurantMenuData> findOne(Long id);

    /**
     * Delete the "id" restaurantMenuData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
