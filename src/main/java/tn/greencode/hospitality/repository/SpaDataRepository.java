package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.SpaData;

/**
 * Spring Data SQL repository for the SpaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpaDataRepository extends JpaRepository<SpaData, Long> {}
