package tn.pfe.CnotConnectV1.services;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tn.pfe.CnotConnectV1.entities.*;
import tn.pfe.CnotConnectV1.repository.PasswordResetTokenRepository;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForAthlete(Athlete athlete,  String passwordToken ){
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, athlete);
        passwordResetTokenRepository.save(passwordResetToken);
    }
  /*  public void createPasswordResetTokenForEmployee( Employee employee, String passwordToken ){
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, employee);
        passwordResetTokenRepository.save(passwordResetToken);
   
    } */
    public void createPasswordResetTokenForUser( User user, String passwordToken ){
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }


//public void createPasswordResetTokenForUser(Personne personne, Agent agent, Parrain parrain, Admin admin, String passwordToken) {
//    if (personne != null) {
//        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, personne);
//        passwordResetTokenRepository.save(passwordResetToken);
//    } else if (agent != null) {
//        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, agent);
//        passwordResetTokenRepository.save(passwordResetToken);
//    } else if (parrain != null) {
//        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, parrain);
//        passwordResetTokenRepository.save(passwordResetToken);
//    } else if (admin != null) {
//        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, admin);
//        passwordResetTokenRepository.save(passwordResetToken);
//    } else {
//        throw new IllegalArgumentException("At least one user entity must be provided.");
//    }
//}


    public String validatePasswordResetTokenAthlete(String theToken){
        PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid password reset token";
        }


        Athlete athlete = token.getAthlete();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "link already expired, resend link";
        }

        return "valid";
    }
    public String validatePasswordResetTokenUser(String theToken){
        PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid password reset token";
        }


        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "link already expired, resend link";
        }

        return "valid";
    }
  /*  public String validatePasswordResetTokenEmployee(String theToken){
        PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid password reset token";
        }


        Employee employee = token.getEmployee();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "link already expired, resend link";
        }

        return "valid";
    }*/
   
//public String validatePasswordResetToken(String theToken) {
//    PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
//    if (token == null) {
//        return "Invalid password reset token";
//    }
//
//    if (token.getPersonne() != null) {
//        Personne personne = token.getPersonne();
//    } else if (token.getAgent() != null) {
//        Agent agent = token.getAgent();
//    } else if (token.getParrain() != null) {
//        Parrain parrain = token.getParrain();
//    } else if (token.getAdmin() != null) {
//        Admin admin = token.getAdmin();
//    }
//
//    Calendar calendar = Calendar.getInstance();
//    if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
//        return "link already expired, resend link";
//    }
//
//    return "valid";
//}


    public Optional<Athlete> findAthleteByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getAthlete());
    }
    public Optional<User> findUserByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getUser());
    }
   /* public Optional<Employee> findEmployeeByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getEmployee());
    }*/
    



    public PasswordResetToken findPasswordResetToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }

}