package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Bars;

/**
 * Spring Data SQL repository for the Bars entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarsRepository extends JpaRepository<Bars, Long> {}
