package tn.pfe.CnotConnectV1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.UserDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to get a user by ID
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (user.getAthlete() != null) {
            Long athleteId = user.getAthlete().getAthleteId();
            System.out.println("Athlete ID: " + athleteId);
            // Return or use the athleteId as needed
        } else {
            System.out.println("No athlete associated with user ID: " + id);
        }

        return user;
    }

    public UserDTO getUserByIds(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        
        Long athleteId = (user.getAthlete() != null) ? user.getAthlete().getAthleteId() : null;

        return new UserDTO(user.getUserId(), user.getUsername(), athleteId);
    }

    public void deleteUser(Long userId) {
        User user  = getUserById(userId);
        userRepository.delete(user);
    }
    
}