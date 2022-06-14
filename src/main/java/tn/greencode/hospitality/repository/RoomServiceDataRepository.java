package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.RoomServiceData;

/**
 * Spring Data SQL repository for the RoomServiceData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomServiceDataRepository extends JpaRepository<RoomServiceData, Long> {}
