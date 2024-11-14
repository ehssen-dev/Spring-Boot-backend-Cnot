package tn.pfe.CnotConnectV1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.*;

@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
	List<Game> findByEvent(Event event);
    List<Game> findByArchive(Archive archive);
	List<Game> findByAthletesContains(Athlete athlete);

}
