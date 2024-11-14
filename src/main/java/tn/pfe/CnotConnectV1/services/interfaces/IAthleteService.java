package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.AthleteDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;


public interface IAthleteService {
	
	
  //  Athlete addAthlete(Athlete c);

   // Athlete updateAthlete(Long athleteId, Athlete updatedAthlete);

    void deleteAthlete(Long athleteId);

    Athlete getAthleteById(Long athleteId);

   // List<Athlete> getAllAthletes();

	void createPasswordResetTokenForAthlete(Athlete athlete, String passwordResetToken);

	Athlete findUserByPasswordToken(String token);

	Optional<Athlete> findByEmail(String email);

	String validatePasswordResetToken(String token);

	void resetPassword(Athlete athlete, String newPassword);

	PerformanceMetrics createPerformanceMetrics(Long athleteId, PerformanceMetrics metrics) throws Exception;

	List<AthleteDTO> getAllAthletes();

	AthleteDTO addAthlete(AthleteDTO athleteDTO);

	AthleteDTO updateAthlete(Long athleteId, AthleteDTO updatedAthleteDTO);

	/**
	 * Retrieves the game associated with the given athlete ID.
	 *
	 * @param athleteId the ID of the athlete
	 * @return the game associated with the athlete, or null if none exists
	 */
	Game getGameByAthleteId(Long athleteId);

	void associateAthleteWithFederation(Long athleteId, Long federationId);
}