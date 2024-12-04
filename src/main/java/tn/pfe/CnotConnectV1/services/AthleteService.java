package tn.pfe.CnotConnectV1.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.AthleteDTO;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Federation;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.PerformanceMetrics;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.FederationRepository;
import tn.pfe.CnotConnectV1.repository.PerformanceMetricsRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IAthleteService;

@Service
public class AthleteService  implements IAthleteService {
    @Autowired
    private final AthleteRepository athleteRepository;
    @Autowired
    private final FederationRepository federationRepository;

    
    @Autowired
    PasswordResetTokenService passwordResetTokenService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    private final PerformanceMetricsRepository performanceMetricsRepository;
    
	private final NotificationService notificationService;
    
    public AthleteService(AthleteRepository athleteRepository, PerformanceMetricsRepository performanceMetricsRepository, NotificationService notificationService, FederationRepository federationRepository) {
        this.athleteRepository = athleteRepository;
		this.federationRepository = federationRepository;
		this.passwordEncoder = null;

		this.performanceMetricsRepository = performanceMetricsRepository;
		this.notificationService = notificationService;
    }
    
    @Override
    public PerformanceMetrics createPerformanceMetrics(Long athleteId, PerformanceMetrics metrics) throws Exception {
    	  Athlete athlete = getAthleteById(athleteId);
    	  metrics.setAthlete(athlete); 
    	  athlete.getPerformanceMetrics().add(metrics); 
    	  athleteRepository.save(athlete); 
    	  return metrics;
    	}
  
    
    public List<String> getAllEmails() {
        return athleteRepository.findAll().stream()
                .map(Athlete::getEmail)
                .collect(Collectors.toList());
    }
    @Override
    public List<AthleteDTO> getAllAthletes() {
        List<Athlete> athletes = athleteRepository.findAll();
        return athletes.stream()
                       .map(athlete -> new AthleteDTO(
                               athlete.getAthleteId(),
                               athlete.getFirstName(),
                               athlete.getLastName(),
                               athlete.getDateOfBirth(),
                               athlete.getEmail(),
                               athlete.getSport()))
                       .collect(Collectors.toList());
    }
    @Override
    public Athlete getAthleteById(Long athleteId) {
        return athleteRepository.findById(athleteId)
                .orElseThrow(() -> new EntityNotFoundException("Athlete with ID " + athleteId + " not found"));
    }
    @Override
    public Optional<Athlete> findByEmail(String email) {
        return athleteRepository.findByEmail(email);
    }
    
    @Override

    public void associateAthleteWithFederation(Long athleteId, Long federationId) {
        Athlete athlete = athleteRepository.findById(athleteId)
            .orElseThrow(() -> new IllegalArgumentException("Athlete not found with ID: " + athleteId));
        Federation federation = federationRepository.findById(federationId)
            .orElseThrow(() -> new IllegalArgumentException("Federation not found with ID: " + federationId));
        
        athlete.joinFederation(federation);

        athleteRepository.save(athlete);
    }
    /**
     * Retrieves the game associated with the given athlete ID.
     *
     * @param athleteId the ID of the athlete
     * @return the game associated with the athlete, or null if none exists
     */
    @Override
    public Game getGameByAthleteId(Long athleteId) {
        Athlete athlete = athleteRepository.findById(athleteId).orElse(null);
        if (athlete == null) {
            throw new IllegalArgumentException("Athlete not found with ID: " + athleteId);
        }
        return athlete.getGame();
    }

	@Override
	 public AthleteDTO addAthlete(AthleteDTO athleteDTO) {
        try {
            Athlete athlete = new Athlete();
            athlete.setFirstName(athleteDTO.getFirstName());
            athlete.setLastName(athleteDTO.getLastName());
            athlete.setDateOfBirth(athleteDTO.getDateOfBirth());
            athlete.setEmail(athleteDTO.getEmail());
            athlete.setSport(athleteDTO.getSport());

            athlete = athleteRepository.save(athlete);

            AthleteDTO savedAthleteDTO = new AthleteDTO(
                athlete.getAthleteId(),
                athlete.getFirstName(),
                athlete.getLastName(),
                athlete.getDateOfBirth(),
                athlete.getEmail(),
                athlete.getSport()
            );

            return savedAthleteDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add athlete", e);
        }
    }
	@Override
	public AthleteDTO updateAthlete(Long athleteId, AthleteDTO updatedAthleteDTO) {
        Athlete existingAthlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("Athlete not found"));

        existingAthlete.setFirstName(updatedAthleteDTO.getFirstName());
        existingAthlete.setLastName(updatedAthleteDTO.getLastName());
        existingAthlete.setDateOfBirth(updatedAthleteDTO.getDateOfBirth());
        existingAthlete.setCity(updatedAthleteDTO.getCity());
        existingAthlete.setGender(updatedAthleteDTO.getGender());
        existingAthlete.setSport(updatedAthleteDTO.getSport());

        Athlete updatedAthlete = athleteRepository.save(existingAthlete);

        AthleteDTO updatedAthleteDTOResponse = new AthleteDTO(
            updatedAthlete.getAthleteId(),
            updatedAthlete.getFirstName(),
            updatedAthlete.getLastName(),
            updatedAthlete.getDateOfBirth(),
            updatedAthlete.getCity(),
            updatedAthlete.getGender(),
            updatedAthlete.getSport()
        );

        return updatedAthleteDTOResponse;
    }
    @Override
    public void deleteAthlete(Long athleteId) {
        athleteRepository.deleteById(athleteId);
    }
    
    
    
    
    @Override
    public Athlete findUserByPasswordToken(String token) {
        return passwordResetTokenService.findAthleteByPasswordToken(token).orElse(null);
    }

    @Override
    public void createPasswordResetTokenForAthlete(Athlete athlete, String passwordResetToken) {
        passwordResetTokenService.createPasswordResetTokenForAthlete(athlete,passwordResetToken);

    }


    @Override
    public String validatePasswordResetToken(String token) {
        return passwordResetTokenService.validatePasswordResetTokenAthlete(token);
    }

    @Override
    public void resetPassword(Athlete athlete, String newPassword) {
    	athlete.setPassword(passwordEncoder.encode(newPassword));
    	athleteRepository.save(athlete);
    }
    
    
}