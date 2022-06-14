package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.RestaurantMenuData;

/**
 * Spring Data SQL repository for the RestaurantMenuData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantMenuDataRepository extends JpaRepository<RestaurantMenuData, Long> {}
