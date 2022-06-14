package tn.greencode.hospitality.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.greencode.hospitality.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
