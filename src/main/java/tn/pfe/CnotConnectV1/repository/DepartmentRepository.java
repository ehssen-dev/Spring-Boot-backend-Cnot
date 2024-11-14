package tn.pfe.CnotConnectV1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Department;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Department findByName(String name);
    Optional<Department> findByNameIgnoreCase(String name);
}