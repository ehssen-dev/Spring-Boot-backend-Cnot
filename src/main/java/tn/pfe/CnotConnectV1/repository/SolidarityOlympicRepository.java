package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;

public interface SolidarityOlympicRepository extends JpaRepository<SolidarityOlympic, Long> {
   
    List<SolidarityOlympic> findByProgramNameContainingIgnoreCase(String programName);
    
    List<SolidarityOlympic> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<SolidarityOlympic> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

   // @Query("SELECT COUNT(s) > 0 FROM SolidarityOlympic s WHERE s.project.id = :projectId AND s.id = :solidarityOlympicId")
   // boolean existsByProjectIdAndId(@Param("projectId") Long projectId, @Param("solidarityOlympicId") Long solidarityOlympicId);    
   // boolean existsByProjectsProject_id(Long projectId);


    
}