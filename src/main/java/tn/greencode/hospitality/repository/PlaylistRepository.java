package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.greencode.hospitality.domain.Playlist;

/**
 * Spring Data SQL repository for the Playlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {}
