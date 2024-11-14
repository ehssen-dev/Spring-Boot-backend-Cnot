package tn.pfe.CnotConnectV1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Athlete;


@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

	Optional<Athlete> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    Optional<Athlete> findByEmail(String email);
    
    
}
