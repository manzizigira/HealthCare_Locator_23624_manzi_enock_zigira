package com.auca.exam.HealthCareLocator.Repository;

import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    List<Hospital> findDistinctHospitalsByTreatedSymptomsIn(Set<Symptom> symptoms);
    List<Hospital> findByTreatedSymptoms(Symptom symptom);

    Page<Hospital> findAll(Pageable pageable);
    Page<Hospital> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
