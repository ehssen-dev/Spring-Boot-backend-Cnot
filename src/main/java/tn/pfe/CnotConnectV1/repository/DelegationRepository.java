package tn.pfe.CnotConnectV1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.*;

@Repository
public interface DelegationRepository extends JpaRepository<Delegation,Long> {
	
    List<Delegation> findByCountry(String country);
    List<Delegation> findByDelegationName(String delegationName);
    List<Delegation> findByAthletes(Athlete athlete);
    Optional<Delegation> findByEmail(String email);

}
