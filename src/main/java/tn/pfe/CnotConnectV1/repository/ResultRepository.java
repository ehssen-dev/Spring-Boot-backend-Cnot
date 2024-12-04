package tn.pfe.CnotConnectV1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tn.pfe.CnotConnectV1.entities.Result;


public interface ResultRepository extends JpaRepository<Result, Long> { 
	@Query("SELECT r, r.game.name FROM Result r")
    List<Object[]> findAllWithGameNames();
}
