package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.RoomService;

/**
 * Spring Data SQL repository for the RoomService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomServiceRepository extends JpaRepository<RoomService, Long> {}
