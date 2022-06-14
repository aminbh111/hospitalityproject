package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.OfferData;

/**
 * Spring Data SQL repository for the OfferData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferDataRepository extends JpaRepository<OfferData, Long> {}
