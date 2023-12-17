package com.auca.exam.HealthCareLocator.Service;

import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Model.User;
import com.auca.exam.HealthCareLocator.Repository.HospitalRepository;
import com.auca.exam.HealthCareLocator.Repository.SymptomRepository;
import com.auca.exam.HealthCareLocator.Repository.UserRepository;
import com.auca.exam.HealthCareLocator.exceptions.EntityNotFoundException;
import com.auca.exam.HealthCareLocator.exceptions.HospitalNotFoundException;
import com.auca.exam.HealthCareLocator.exceptions.SymptomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SymptomService {

    @Autowired
    private final SymptomRepository symptomRepository;

    @Autowired
    private final HospitalRepository hospitalRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public SymptomService(SymptomRepository symptomRepository, HospitalRepository hospitalRepository, UserRepository userRepository) {
        this.symptomRepository = symptomRepository;
        this.hospitalRepository = hospitalRepository;
        this.userRepository = userRepository;
    }

    public List<Hospital> findHospitalsByMultipleSymptoms(Set<Symptom> symptoms) {
        if (symptoms.isEmpty()) {
            return Collections.emptyList();
        }

        Iterator<Symptom> symptomIterator = symptoms.iterator();
        Set<Hospital> commonHospitals = new HashSet<>(getHospitalsBySymptom(symptomIterator.next()));

        while (symptomIterator.hasNext()) {
            Set<Hospital> hospitalsForSymptom = getHospitalsBySymptom(symptomIterator.next());
            commonHospitals.retainAll(hospitalsForSymptom);
        }

        return new ArrayList<>(commonHospitals);
    }

    private Set<Hospital> getHospitalsBySymptom(Symptom symptom) {
       return symptom.getHospitals();
    }

    public List<Symptom> searchSymptoms(String name) {
        // Implement your search logic here using the repository
        return symptomRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Symptom> getAllSymptoms() {
        return symptomRepository.findAll();
    }

    public Optional<Symptom> findSymptomById(Long id) {
        return symptomRepository.findById(id);
    }

    public Symptom addNewSymptom(Symptom newSymptom) {
     return symptomRepository.save(newSymptom);
    }

    public Symptom updateSymptom(Long symptomId, Symptom updatedSymptomDetails) {
        Optional<Symptom> optionalSymptom = symptomRepository.findById(symptomId);

        if (optionalSymptom.isPresent()) {
            Symptom existingSymptom = optionalSymptom.get();

            existingSymptom.setName(updatedSymptomDetails.getName());
            existingSymptom.setId(updatedSymptomDetails.getId());
            existingSymptom.setHospitals(updatedSymptomDetails.getHospitals());

            return symptomRepository.save(existingSymptom);
        } else {
          throw new RuntimeException("Symptom not found with ID: " + symptomId);
        }
    }

    public List<Symptom> findSymptomsByIds(List<Long> symptomIds) {
        return symptomRepository.findAllById(symptomIds);
    }


    public void deleteSymptomById(Long symptomId) {
        Optional<Symptom> optionalSymptom = symptomRepository.findById(symptomId);

        if (optionalSymptom.isPresent()) {
            Symptom symptomToDelete = optionalSymptom.get();
            symptomRepository.delete(symptomToDelete);
        } else {
           throw new RuntimeException("Symptom not found with ID: " + symptomId);
        }
    }

    public List<Hospital> findHospitalsBySymptom(Symptom symptom) {
        return hospitalRepository.findByTreatedSymptoms(symptom);
    }



    public void addSymptomToHospital(Long hospitalId, Long symptomId) {
        Optional<Hospital> optionalHospital = hospitalRepository.findById(hospitalId);
        Optional<Symptom> optionalSymptom = symptomRepository.findById(symptomId);

        if (optionalHospital.isPresent() && optionalSymptom.isPresent()) {
            Hospital hospital = optionalHospital.get();
            Symptom symptom = optionalSymptom.get();

            hospital.getTreatedSymptoms().add(symptom);
            hospitalRepository.save(hospital);
        } else {
            throw new EntityNotFoundException("Hospital or Symptom not Found!");
        }
    }

    public void removeSymptomFromHospital(Long hospitalId, Long symptomId) throws HospitalNotFoundException, SymptomNotFoundException {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new HospitalNotFoundException("Hospital not found with ID: " + hospitalId));

        Symptom symptom = symptomRepository.findById(symptomId)
                .orElseThrow(() -> new SymptomNotFoundException("Symptom not found with ID: " + symptomId));

        if (hospital.getTreatedSymptoms().contains(symptom)) {
            hospital.getTreatedSymptoms().remove(symptom);
            hospitalRepository.save(hospital);
        } else {
            throw new SymptomNotFoundException("Symptom with ID " + symptomId + " is not associated with Hospital ID " + hospitalId);
        }
    }

    public Set<Symptom> findSymptomsByIds(Set<Long> symptomIds) {
        return symptomRepository.findByIdIn(symptomIds);
    }

    public Symptom getSymptomById(Long id) {
        return symptomRepository.findById(id)
                .orElseThrow(() -> new SymptomNotFoundException("Symptom not found with ID: " + id));
    }


}
