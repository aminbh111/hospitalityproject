package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.ServiceData;

/**
 * Spring Data SQL repository for the ServiceData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceDataRepository extends JpaRepository<ServiceData, Long> {}
