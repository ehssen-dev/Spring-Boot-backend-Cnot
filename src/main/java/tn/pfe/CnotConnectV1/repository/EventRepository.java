package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Archive;
import tn.pfe.CnotConnectV1.entities.Event;


@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
	List<Event> findByName(String name);
    List<Event> findByStartEventBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByEndEventBetween(LocalDate startDate, LocalDate endDate);
    List<Event> findByArchive(Archive archive);
    @Query("SELECT e FROM Event e WHERE e.endEvent < CURRENT_DATE")
    List<Event> findAllEndedEvents();
}
