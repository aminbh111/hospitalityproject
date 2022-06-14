package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.BarsData;

/**
 * Spring Data SQL repository for the BarsData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarsDataRepository extends JpaRepository<BarsData, Long> {}
