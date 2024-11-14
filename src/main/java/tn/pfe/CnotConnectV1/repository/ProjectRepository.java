package tn.pfe.CnotConnectV1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.SolidarityOlympic;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.enums.ProjectStatus;



@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    
    List<Project> findByProjectName(String projectName);
    
    List<Project> findByDepartment(Department department);
    
    List<Project> findByStatus(ProjectStatus status);
    
    List<Project> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Project> findByEndDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Project> findByValidationDateBetween(LocalDate startDate, LocalDate endDate);
    
  //  List<Project> findByIncludedInProgram(Boolean includedInProgram);
   // List<Project> findBysolidarity_olympic_id(Long solidarityOlympicId);
   // List<Project> findByIncludedInProgramAndSolidarityOlympic_id(Boolean includedInProgram, Long solidarityOlympicId);
    @Query("SELECT p FROM Project p WHERE p.includedInProgram = :includedInProgram AND p.solidarityOlympic = :solidarityOlympic")
    List<Project> findByIncludedInProgramAndSolidarityOlympic(@Param("includedInProgram") Boolean includedInProgram, @Param("solidarityOlympic") SolidarityOlympic solidarityOlympic);
    List<Project> findByTechnicalReportSubmitted(boolean technicalReportSubmitted);
    
    List<Project> findByFinancialReportSubmitted(boolean financialReportSubmitted);
   

}