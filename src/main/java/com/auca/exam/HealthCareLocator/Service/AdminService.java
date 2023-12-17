package com.auca.exam.HealthCareLocator.Service;

import com.auca.exam.HealthCareLocator.Model.Admin;
import com.auca.exam.HealthCareLocator.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;



    public Admin login(String username, String password) {
        Admin user = adminRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login successful
        }
        return null; // Invalid credentials
    }

    // Method to handle user registration
    public Admin register(Admin newUser) {
        return adminRepository.save(newUser);
    }
}
