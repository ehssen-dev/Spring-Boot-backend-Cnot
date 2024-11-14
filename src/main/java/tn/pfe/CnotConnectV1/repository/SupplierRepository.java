package tn.pfe.CnotConnectV1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.Supplier;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

	//List<Supplier> findByRequestedGoods(String requestedGoods);
}

