package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Service;

/**
 * Spring Data SQL repository for the Service entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {}
