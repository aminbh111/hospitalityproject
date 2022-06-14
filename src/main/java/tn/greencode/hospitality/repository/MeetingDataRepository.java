package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.MeetingData;

/**
 * Spring Data SQL repository for the MeetingData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingDataRepository extends JpaRepository<MeetingData, Long> {}
