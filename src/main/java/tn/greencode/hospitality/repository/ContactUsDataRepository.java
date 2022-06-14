package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.ContactUsData;

/**
 * Spring Data SQL repository for the ContactUsData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactUsDataRepository extends JpaRepository<ContactUsData, Long> {}
