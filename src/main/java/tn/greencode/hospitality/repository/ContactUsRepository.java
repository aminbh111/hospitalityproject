package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.ContactUs;

/**
 * Spring Data SQL repository for the ContactUs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {}
