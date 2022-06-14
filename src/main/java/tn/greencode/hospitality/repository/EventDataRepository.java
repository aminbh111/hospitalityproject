package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.EventData;

/**
 * Spring Data SQL repository for the EventData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventDataRepository extends JpaRepository<EventData, Long> {}
