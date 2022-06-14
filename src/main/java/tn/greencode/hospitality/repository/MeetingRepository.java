package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Meeting;

/**
 * Spring Data SQL repository for the Meeting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {}
