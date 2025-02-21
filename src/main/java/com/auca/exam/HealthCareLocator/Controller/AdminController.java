package com.auca.exam.HealthCareLocator.Controller;

import com.auca.exam.HealthCareLocator.Model.Admin;
import com.auca.exam.HealthCareLocator.Model.User;
import com.auca.exam.HealthCareLocator.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/view")
    public String index(){
        return "/Admin/Admin";
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam String username, @RequestParam String password) {
        // Authenticate user using the UserService
        Admin authenticatedUser = adminService.login(username, password);

        if (authenticatedUser != null) {
            // If authentication succeeds, you can redirect to a dashboard or another page
            return "/Admin/AdminHome"; // Redirect to the dashboard page
        } else {
            // If authentication fails, you can redirect back to the login page with an error message
            throw new RuntimeException("ERROR!");
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password) {
        Admin newUse = new Admin();
        newUse.setUsername(username);
        newUse.setPassword(password);
        adminService.register(newUse);
        return "/Admin/Admin";
    }

    @GetMapping("/log")
    public String Login(){
        return "/Client/Login";
    }

    @GetMapping("/home")
    public String adminHome(){
        return "/Admin/AdminHome";
    }

}
