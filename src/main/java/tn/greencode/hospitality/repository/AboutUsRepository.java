package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.AboutUs;

/**
 * Spring Data SQL repository for the AboutUs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutUsRepository extends JpaRepository<AboutUs, Long> {}
