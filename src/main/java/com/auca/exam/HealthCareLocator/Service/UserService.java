package com.auca.exam.HealthCareLocator.Service;

import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Model.User;
import com.auca.exam.HealthCareLocator.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final SymptomService symptomService; // Inject SymptomService
    @Autowired
    private final HospitalService hospitalService;

    public UserService(UserRepository userRepository, SymptomService symptomService, HospitalService hospitalService) {
        this.userRepository = userRepository;
        this.symptomService = symptomService;
        this.hospitalService = hospitalService;

    }

    // Method to handle user login
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login successful
        }
        return null; // Invalid credentials
    }

    // Method to handle user registration
    public User register(User newUser) {
        return userRepository.save(newUser);
    }

    // Method to manage user's selected symptoms
    public List<User> getAll(){
        return userRepository.findAll();
    }

}

