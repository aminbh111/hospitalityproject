package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Concierge;

/**
 * Spring Data SQL repository for the Concierge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConciergeRepository extends JpaRepository<Concierge, Long> {}
