package tn.pfe.CnotConnectV1.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.pfe.CnotConnectV1.configuration.services.UserDetailsImpl;
import tn.pfe.CnotConnectV1.dto.UserDTO;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.exeptions.BudgetAllocationNotFoundException;
import tn.pfe.CnotConnectV1.repository.UserRepository;
import tn.pfe.CnotConnectV1.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getRoles().stream()
                                  .map(role -> role.getName().name())
                                  .collect(Collectors.toList());

        
        Long athleteId = null;
        if (user.getAthlete() != null) {
            athleteId = user.getAthlete().getAthleteId();
        }

        UserInfoResponse response = new UserInfoResponse(user.getUsername(), roles, athleteId);

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{identifier}")
    public User getUserByIdentifier(@PathVariable String identifier) {
        System.out.println("Fetching user with identifier: " + identifier); 
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user identifier"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/s/{id}")
    public ResponseEntity<UserDTO> getUserByIds(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserByIds(id);
        return ResponseEntity.ok(userDTO);
    }
  
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteBudgetAllocation(@PathVariable Long userId) {
        try {
        	userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (BudgetAllocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    public class UserInfoResponse {
        private String username;
        private List<String> roles;
        private Long athleteId; 

        public UserInfoResponse(String username, List<String> roles, Long athleteId) {
            this.username = username;
            this.roles = roles;
            this.athleteId = athleteId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public Long getAthleteId() {
            return athleteId;
        }

        public void setAthleteId(Long athleteId) {
            this.athleteId = athleteId;
        }
    }
}
