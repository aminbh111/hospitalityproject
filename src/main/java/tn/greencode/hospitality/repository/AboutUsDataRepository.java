package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.AboutUsData;

/**
 * Spring Data SQL repository for the AboutUsData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AboutUsDataRepository extends JpaRepository<AboutUsData, Long> {}
