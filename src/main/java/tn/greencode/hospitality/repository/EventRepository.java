package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Event;

/**
 * Spring Data SQL repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {}
