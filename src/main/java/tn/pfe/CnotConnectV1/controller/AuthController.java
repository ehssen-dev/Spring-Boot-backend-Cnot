package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.configuration.jwt.JwtUtils;
import tn.pfe.CnotConnectV1.configuration.services.UserDetailsImpl;
import tn.pfe.CnotConnectV1.entities.Athlete;
import tn.pfe.CnotConnectV1.entities.Delegation;
import tn.pfe.CnotConnectV1.entities.ERole;
import tn.pfe.CnotConnectV1.entities.Role;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.payload.request.LoginRequest;
import tn.pfe.CnotConnectV1.payload.request.SignupRequest;
import tn.pfe.CnotConnectV1.payload.response.JwtResponse;
import tn.pfe.CnotConnectV1.payload.response.MessageResponse;
import tn.pfe.CnotConnectV1.repository.AthleteRepository;
import tn.pfe.CnotConnectV1.repository.DelegationRepository;
import tn.pfe.CnotConnectV1.repository.RoleRepository;
import tn.pfe.CnotConnectV1.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000") 
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;
  @Autowired
  AthleteRepository athleteRepository;
  @Autowired
  DelegationRepository delegationRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	  System.out.println("Received login request: " + loginRequest.getUsername());

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority()) 
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
      if (userRepository.existsByUsername(signUpRequest.getUsername())) {
          return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
      }

      if (userRepository.existsByEmail(signUpRequest.getEmail())) {
          return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
      }

      User user = new User(signUpRequest.getUsername(),
                          signUpRequest.getEmail(),
                          encoder.encode(signUpRequest.getPassword()));

      Set<Role> roles = new HashSet<>();
      String rolePrefix = "U"; // Default prefix for USER

      Optional<Athlete> athleteOpt = athleteRepository.findByEmail(signUpRequest.getEmail());
      if (athleteOpt.isPresent()) {
       
          Role athleteRole = roleRepository.findByName(ERole.ROLE_ATHLETE)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(athleteRole);
          rolePrefix = "AT";
        
          Athlete athlete = athleteOpt.get();
          user.setAthlete(athlete); 
          athlete.setUser(user); 
      } else {
          
          Optional<Delegation> delegationOpt = delegationRepository.findByEmail(signUpRequest.getEmail());
          if (delegationOpt.isPresent()) {
             
              Role delegationRole = roleRepository.findByName(ERole.ROLE_DELEGATION)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(delegationRole);
              rolePrefix = "DE";
             
              Delegation delegation = delegationOpt.get();
              user.setDelegation(delegation);
              delegation.setUser(user);
          } else {
              
              Set<String> strRoles = signUpRequest.getRole();
              if (strRoles == null) {
                  Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                      .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                  roles.add(userRole);
              } else {
                  for (String role : strRoles) {
                      switch (role) {
                          case "admin":
                              Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                              roles.add(adminRole);
                              rolePrefix = "AD";
                              break;
                          case "moderator":
                              Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                              roles.add(modRole);
                              rolePrefix = "MO";
                              break;
                          case "athlete":
                              Role athleteRole = roleRepository.findByName(ERole.ROLE_ATHLETE)
                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                              roles.add(athleteRole);
                              rolePrefix = "AT";
                              break;
                          case "delegation":
                              Role delegationRole = roleRepository.findByName(ERole.ROLE_DELEGATION)
                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                              roles.add(delegationRole);
                              rolePrefix = "DE";
                              break;
                          default:
                              Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                              roles.add(userRole);
                              break;
                      }
                  }
              }
          }
      }

      String uniqueIdentifier = generateIdentifier(rolePrefix);
      user.setIdentifier(uniqueIdentifier);

      user.setRoles(roles);
      userRepository.save(user);

      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  private static final int LENGTH = 7;
  private static final Random RANDOM = new Random();

  public static String generateIdentifier(String prefix) {
      StringBuilder identifier = new StringBuilder(prefix);
      for (int i = 0; i < LENGTH; i++) {
          identifier.append(RANDOM.nextInt(10)); 
      }
      return identifier.toString();
  }
  
}