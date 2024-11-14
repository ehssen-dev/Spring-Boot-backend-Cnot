package tn.pfe.CnotConnectV1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.pfe.CnotConnectV1.entities.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String theToken);
}
      