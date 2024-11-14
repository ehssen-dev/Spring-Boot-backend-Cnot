package tn.pfe.CnotConnectV1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pfe.CnotConnectV1.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //User findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    Optional<User> getByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    
    Optional<User> findByIdentifier(String identifier);
    

}