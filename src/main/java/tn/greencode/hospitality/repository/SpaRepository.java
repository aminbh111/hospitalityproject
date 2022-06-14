package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Spa;

/**
 * Spring Data SQL repository for the Spa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpaRepository extends JpaRepository<Spa, Long> {}
