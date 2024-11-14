package tn.pfe.CnotConnectV1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.SupportResponse;

@Repository
public interface SupportResponseRepository extends JpaRepository< SupportResponse, Long>{

}
