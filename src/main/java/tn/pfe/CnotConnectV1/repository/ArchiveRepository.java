package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Archive;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    Optional<Archive> findByName(String name);
    Optional<Archive> findByDate(LocalDate date);
    List<Archive> findByProjectIsNotNull();
}