package com.auca.exam.HealthCareLocator.Service;

import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;


import java.math.BigDecimal;
import java.util.*;

@Service
public class HospitalService {

    @Autowired
    private final HospitalRepository hospitalRepository;

    @Autowired
    private SymptomService symptomService;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<Hospital> findHospitalsBySymptoms(BigDecimal userLatitude, BigDecimal userLongitude, Set<Symptom> symptoms) {
        List<Hospital> hospitals = hospitalRepository.findDistinctHospitalsByTreatedSymptomsIn(symptoms);
        List<String> directionsUrls = generateGoogleMapsDirectionsUrls(userLatitude, userLongitude, hospitals);

        // Assign Google Maps URLs to hospitals
        for (int i = 0; i < hospitals.size(); i++) {
            hospitals.get(i).setGoogleMapsUrl(directionsUrls.get(i));
            // Additionally, you can set latitude, longitude, or any other relevant information here
        }

        return hospitals;
    }


    private List<String> generateGoogleMapsDirectionsUrls(BigDecimal userLatitude, BigDecimal userLongitude, List<Hospital> hospitals) {
        String apiKey = "AIzaSyDW3bie7VoIk8EAX9TN98G1onMzPPoEOoE"; // Replace with your actual Google Maps API key
        List<String> directionsUrls = new ArrayList<>();

        String origin = userLatitude + "," + userLongitude; // User's location

        for (Hospital hospital : hospitals) {
            BigDecimal hospitalLatitude = hospital.getLatitude();
            BigDecimal hospitalLongitude = hospital.getLongitude();

            String destination = hospitalLatitude + "," + hospitalLongitude; // Hospital's location

            // Construct the URL for directions using the API key, origin, and destination
            String url = "https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + destination + "&key=" + apiKey;
            directionsUrls.add(url);
        }

        return directionsUrls;
    }


    public Hospital addHospital(String name, String address, BigDecimal latitude, BigDecimal longitude, Map<String, String> operatingHours, Set<Symptom> treatedSymptoms) {
        Hospital newHospital = new Hospital();
        newHospital.setName(name);
        newHospital.setAddress(address);
        newHospital.setLatitude(latitude);
        newHospital.setLongitude(longitude);
        newHospital.setOperatingHours(operatingHours);
        newHospital.setTreatedSymptoms(treatedSymptoms);

        // Save the newly created hospital using the repository
        return hospitalRepository.save(newHospital);
    }

    public Optional<Hospital> getHospitalById(Long hospitalId) {
        return hospitalRepository.findById(hospitalId);
    }

    public List<Hospital> getAllHospital(){
        return hospitalRepository.findAll();
    }

    public Hospital updateHospitalDetails(Long hospitalId, Hospital updatedHospitalDetails) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new NoSuchElementException("Hospital not found"));

        // Update the hospital details
        hospital.setName(updatedHospitalDetails.getName());
        hospital.setAddress(updatedHospitalDetails.getAddress());
        hospital.setLatitude(updatedHospitalDetails.getLatitude());
        hospital.setLongitude(updatedHospitalDetails.getLongitude());
        hospital.setOperatingHours(updatedHospitalDetails.getOperatingHours());
        hospital.setTreatedSymptoms(updatedHospitalDetails.getTreatedSymptoms());

        return hospitalRepository.save(hospital);
    }

    public void deleteHospitalById(Long hospitalId) {
        Optional<Hospital> hospitalOptional = hospitalRepository.findById(hospitalId);
        hospitalOptional.ifPresent(hospitalRepository::delete);
        // If needed, handle scenarios where hospital is not found.
    }

    public String getOperatingHoursOfHospital(Long hospitalId) {
        Optional<Hospital> hospitalOptional = hospitalRepository.findById(hospitalId);
        if (hospitalOptional.isPresent()) {
            Hospital hospital = hospitalOptional.get();
            Map<String, String> operatingHours = hospital.getOperatingHours();
            return convertOperatingHoursToString(operatingHours);
        }
        return "Operating hours not found for the hospital ID: " + hospitalId;
    }


    private String convertOperatingHoursToString(Map<String, String> operatingHours) {
        // Convert the operating hours map to a desired string format
        // For example, you can use Jackson ObjectMapper to convert to JSON
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(operatingHours);
        } catch (JsonProcessingException e) {
            // Handle exception if conversion fails
            e.printStackTrace();
            return "Error converting operating hours to string";
        }
    }

    public List<Hospital> findNearestHospitals(BigDecimal userLatitude, BigDecimal userLongitude) {
        List<Hospital> hospitals = hospitalRepository.findAll(); // Fetch all hospitals

        // Calculate distances and sort by proximity to user
        hospitals.sort(Comparator.comparingDouble(hospital ->
                calculateDistance(userLatitude, userLongitude,
                        hospital.getLatitude(), hospital.getLongitude())));

        return hospitals;
    }

    private double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        // Implement a method to calculate distance between two geographic coordinates
        // Using a suitable algorithm like Haversine formula or Vincenty formula

        // Sample implementation of Haversine formula
        double earthRadius = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance; // Distance in kilometers
    }

    public Hospital addHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Hospital saveHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Hospital updateHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }


//    public List<Hospital> findHospitalsBySymptomIds(BigDecimal userLatitude, BigDecimal userLongitude, List<Long> symptomIds) {
//        // Retrieve all symptoms corresponding to the provided IDs
//        List<Symptom> symptoms = symptomService.findSymptomsByIds(symptomIds);
//
//        // Find hospitals treating the specified symptoms
//        List<Hospital> hospitalsTreatingSymptoms = new ArrayList<>();
//
//        for (Symptom symptom : symptoms) {
//            List<Hospital> hospitalsForSymptom = hospitalRepository.findByTreatedSymptoms(symptom);
//            hospitalsTreatingSymptoms.addAll(hospitalsForSymptom);
//        }
//
//        // Generate Google Maps URLs based on hospital information
//        List<String> googleMapsUrls = generateGoogleMapsUrls(userLatitude, userLongitude, hospitalsTreatingSymptoms);
//
//        // Set Google Maps URLs for hospitals
//        for (int i = 0; i < hospitalsTreatingSymptoms.size(); i++) {
//            Hospital hospital = hospitalsTreatingSymptoms.get(i);
//            String googleMapsUrl = googleMapsUrls.get(i);
//            hospital.setGoogleMapsUrl(googleMapsUrl);
//        }
//
//        return hospitalsTreatingSymptoms;
//    }

    // Method to generate Google Maps URLs based on hospital information
    public List<Hospital> findHospitalsBySymptomIds(BigDecimal userLatitude, BigDecimal userLongitude, List<Long> symptomIds) {
        // Retrieve all symptoms corresponding to the provided IDs
        List<Symptom> symptoms = symptomService.findSymptomsByIds(symptomIds);

        // Find hospitals treating the specified symptoms along with Google Maps URLs
        List<Hospital> hospitalsTreatingSymptoms = new ArrayList<>();

        for (Symptom symptom : symptoms) {
            List<Hospital> hospitalsForSymptom = hospitalRepository.findByTreatedSymptoms(symptom);
            // Ensure the 'googleMapsUrl' field is populated when fetching hospitals
            hospitalsTreatingSymptoms.addAll(hospitalsForSymptom);
        }

        return hospitalsTreatingSymptoms;
    }

    public Page<Hospital> listHospitalsPaginated(int pageNo, int pageSize, String sortField, String sortDir, String searchTerm) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        if (StringUtils.hasText(searchTerm)) {
            Page<Hospital> searchResult = hospitalRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
            // Log the results and search term
            System.out.println("Search Term: " + searchTerm);
            System.out.println("Search Result: " + searchResult.getContent());
            return searchResult;
        } else {
            // Log the values when there's no search term
            System.out.println("No Search Term");
            Page<Hospital> allHospital = hospitalRepository.findAll(pageable);
            System.out.println("All Hospital: " + allHospital.getContent());
            return allHospital;
        }
    }

    public List<Hospital>listhospita(){
        return hospitalRepository.findAll();
    }

}
