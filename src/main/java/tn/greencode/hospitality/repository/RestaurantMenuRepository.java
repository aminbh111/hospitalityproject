package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.RestaurantMenu;

/**
 * Spring Data SQL repository for the RestaurantMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {}
