package com.auca.exam.HealthCareLocator.Repository;

import com.auca.exam.HealthCareLocator.Model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long>{
    Set<Symptom> findByIdIn(Set<Long> symptomIds);
    List<Symptom> findByNameContainingIgnoreCase(String name);
}
