package com.auca.exam.HealthCareLocator.Controller;
import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Model.User;
import com.auca.exam.HealthCareLocator.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/view")
    public String index2(){
        return "/Client/Login";
    }

    @GetMapping("/admin")
    public String admin(){
        return "/Admin/Admin";
    }

    @GetMapping("/AddUsersPage")
    public String addPage(){
        return "/Admin/Users/AddUsers";
    }

    @GetMapping("/viewsPage")
    public String viewPage(Model model)
    {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "/Admin/Users/ViewUsers";
    }

    @GetMapping("/viewed")
    public String homePage(){
        return "/Client/HomePage";
    }
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username, @RequestParam("password") String password) {
      User newUse = new User();
      newUse.setUsername(username);
      newUse.setPassword(password);
      userService.register(newUse);
      return "/Client/Login";
    }


    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        // Authenticate user using the UserService
        User authenticatedUser = userService.login(username, password);

        if (authenticatedUser != null) {
            // Get the username of the authenticated user
            String loggedInUserName = authenticatedUser.getUsername();

            // Add the username to the model attribute
            model.addAttribute("loggedInUserName", loggedInUserName);

            // If authentication succeeds, redirect to the client page
            return "Client/HomePage"; // Assuming "client" is the endpoint for the client page
        } else {
            // If authentication fails, you can redirect back to the login page with an error message
            throw new RuntimeException("ERROR!");
        }
    }


    private User getUserById(Long userId) {
        throw new RuntimeException("User not found with ID: " + userId);
    }
}
