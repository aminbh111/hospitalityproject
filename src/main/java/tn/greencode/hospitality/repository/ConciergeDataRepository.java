package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.ConciergeData;

/**
 * Spring Data SQL repository for the ConciergeData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConciergeDataRepository extends JpaRepository<ConciergeData, Long> {}
