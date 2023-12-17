package com.auca.exam.HealthCareLocator.Repository;

import com.auca.exam.HealthCareLocator.Model.User;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
