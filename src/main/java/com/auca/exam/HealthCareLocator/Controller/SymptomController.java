package com.auca.exam.HealthCareLocator.Controller;
import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Model.User;
import com.auca.exam.HealthCareLocator.Service.HospitalService;
import com.auca.exam.HealthCareLocator.Service.SymptomService;
import com.auca.exam.HealthCareLocator.exceptions.HospitalNotFoundException;
import com.auca.exam.HealthCareLocator.exceptions.SymptomNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/symptoms")
public class SymptomController {

    @Autowired
    private final SymptomService symptomService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    public SymptomController(SymptomService symptomService) {
        this.symptomService = symptomService;

    }

    @GetMapping("/search")
    public ResponseEntity<List<Symptom>> searchSymptoms(@RequestParam("name") String name) {
        List<Symptom> foundSymptoms = symptomService.searchSymptoms(name);
        return ResponseEntity.ok(foundSymptoms);
    }

    @GetMapping("/symptom/{id}")
    public String getSymptomDetails(@PathVariable Long id, Model model) {
        // Get symptom details based on the ID
        Symptom symptom = symptomService.findSymptomById(id)
                .orElseThrow(() -> new RuntimeException("Symptom not found with ID: " + id));

        model.addAttribute("symptom", symptom);
        return "/Admin/Symptom/AddSymptom"; // Return the name of the Thymeleaf template for displaying symptom details
    }


    @GetMapping("/alls")
    public List<Symptom> getAllSymptomss() {
        return symptomService.getAllSymptoms();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Symptom>> getAllSymptoms() {
        List<Symptom> symptoms = symptomService.getAllSymptoms();
        return ResponseEntity.ok(symptoms);
    }

    @PostMapping("/add")
    public String addNewSymptom(HttpServletRequest request) {
        String name = request.getParameter("name");
        Symptom newSymptom = new Symptom();
        newSymptom.setName(name);

        Symptom symptom = symptomService.addNewSymptom(newSymptom);
        if (symptom != null) {
            return "/Admin/AdminHome";
        } else {
            throw new RuntimeException("Failed to add symptom!");
        }
    }

    @PutMapping("/{symptomId}")
    public Symptom updateSymptom(@PathVariable Long symptomId, @RequestBody Symptom updatedSymptomDetails) {
        return symptomService.updateSymptom(symptomId, updatedSymptomDetails);
    }

    @DeleteMapping("/{symptomId}")
    public void deleteSymptomById(@PathVariable Long symptomId) {
        symptomService.deleteSymptomById(symptomId);
    }

    @GetMapping("/{symptomId}")
    public Symptom getSymptomById(@PathVariable Long symptomId) {
        return symptomService.findSymptomById(symptomId)
                .orElseThrow(() -> new RuntimeException("Symptom not found with ID: " + symptomId));
    }

    @GetMapping("/hospitals")
    public List<Hospital> findHospitalsByMultipleSymptoms(@RequestBody Set<Symptom> symptoms) {
        return symptomService.findHospitalsByMultipleSymptoms(symptoms);
    }

    @GetMapping("/{symptomId}/hospitals")
    public List<Hospital> findHospitalsBySymptom(@PathVariable Long symptomId) {
        Symptom symptom = symptomService.findSymptomById(symptomId)
                .orElseThrow(() -> new RuntimeException("Symptom not found with ID: " + symptomId));
        return symptomService.findHospitalsBySymptom(symptom);
    }


    @GetMapping("/selectSymptoms")
    public String selectSymptoms(Model model) {
        List<Symptom> symptoms = symptomService.getAllSymptoms();
        model.addAttribute("symptoms", symptoms);
        return "/Admin/Symptom/symptomSelecting"; // Replace "/path/to/your/html/file" with the path to your HTML file
    }


//    @PostMapping("/hospitals")
//    public String findHospitalsBySymptomsAndLocation(@RequestParam("userLatitude") BigDecimal userLatitude,
//                                                     @RequestParam("userLongitude") BigDecimal userLongitude,
//                                                     @RequestParam("symptom1") Long symptom1Id,
//                                                     Model model) {
//        // Fetch symptom based on ID
//        Symptom symptom1 = symptomService.findSymptomById(symptom1Id).orElse(null);
//
//        if (symptom1 != null) {
//            Set<Symptom> selectedSymptoms = new HashSet<>(Collections.singletonList(symptom1));
//
//            // Find hospitals based on the selected symptom and user's location
//            List<Hospital> hospitals = hospitalService.findHospitalsBySymptoms(userLatitude, userLongitude, selectedSymptoms);
//
//            // Pass hospitals to the view
//            model.addAttribute("hospitals", hospitals);
//
//            return "Admin/Hospital/nearestHospital"; // Return the name of your HTML template showing the nearest hospitals
//        } else {
//            // Handle if symptom is not found
//            return "Admin/Symptom/symptomSelecting"; // Redirect to an error page or handle it accordingly
//        }
//    }

    @PostMapping("/hospitals")
    public String findHospitalsBySymptomIds(@RequestParam("userLatitude") BigDecimal userLatitude,
                                            @RequestParam("userLongitude") BigDecimal userLongitude,
                                            @RequestParam("symptomIds") List<Long> symptomIds,
                                            Model model) {
        List<Hospital> hospitals = hospitalService.findHospitalsBySymptomIds(userLatitude, userLongitude, symptomIds);
        model.addAttribute("hospitals", hospitals);
        return "Admin/Hospital/nearestHospital";
    }


    @GetMapping("/hospital")
    public String getHospitalsHTML(Model model) {
        List<Hospital> hospitals = hospitalService.getAllHospital(); // Get hospitals from service

        model.addAttribute("hospitals", hospitals); // Add hospitals to model

        return "hospitals"; // Return the name of the HTML template
    }

    @GetMapping("/nearestHospitals")
    public String getNearestHospitals(Model model) {
        List<Symptom> symptoms = symptomService.getAllSymptoms(); // Fetch symptoms from the service

        model.addAttribute("symptoms", symptoms);

        return "/Admin/Symptom/symptomSelecting"; // Return the Thymeleaf HTML template
    }


    @PostMapping("/batch")
    public Set<Symptom> findSymptomsByIds(@RequestBody Set<Long> symptomIds) {
        return symptomService.findSymptomsByIds(symptomIds);
    }

    @PostMapping("/addSymptomToHospital")
    public void addSymptomToHospital(@RequestParam Long hospitalId, @RequestParam Long symptomId) {
        symptomService.addSymptomToHospital(hospitalId, symptomId);
    }

    @DeleteMapping("/hospitals/{hospitalId}/symptoms/{symptomId}")
    public void removeSymptomFromHospital(@PathVariable Long hospitalId, @PathVariable Long symptomId) {
        try {
            symptomService.removeSymptomFromHospital(hospitalId, symptomId);
        } catch (HospitalNotFoundException | SymptomNotFoundException e) {
            // Handle the exception as needed, e.g., return an error response
            // For example:
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            throw new RuntimeException(e.getMessage()); // Re-throwing as a RuntimeException for simplicity
        }
    }

    @GetMapping("/symptom")
    public String symptomPage(){
        return "/Admin/Symptom/AddSymptom";
    }

    @GetMapping("/symptomByHospital")
    public String symptomHospitalPage(){
        return "/Admin/Symptom/AddSymptomByHospital";
    }

    @GetMapping("/findHospitalBySymptom")
    public String findHospBySymp(){
        return "/Admin/Symptom/FindHospitalsBySymptom";
    }

    @GetMapping("/RemoveSymptomsFromHospitalsPage")
    public String removeSymptomsFromHospitalsPage(){
        return "/Admin/Symptom/RemoveSymptomsFromHospitals";
    }

    @GetMapping("/updateSymptom/{id}")
    public String getUpdateForm(@PathVariable String id, Model model){
        Symptom symptom = symptomService.findSymptomById(Long.parseLong(id)).orElse(null);
        model.addAttribute("symptom", symptom);
        return "/Admin/Symptom/UpdateSymptom";
    }

    @PostMapping("/updateSymptom/{id}")
    public String updateSymptom(@PathVariable Long id, @ModelAttribute("symptom") Symptom updatedSymptom, Model model) {
        Symptom existingSymptom = symptomService.findSymptomById(id).orElse(null);
        if (existingSymptom != null) {
            // Update the existing symptom with the new values
            existingSymptom.setName(updatedSymptom.getName());
            // You can update other fields similarly

            // Save the updated symptom
            symptomService.addNewSymptom(existingSymptom);
            model.addAttribute("symptoms", symptomService.getAllSymptoms());
            return "/Admin/Symptom/ViewSymptom"; // Return to the view after updating
        } else {
            // Handle if the symptom with the given ID is not found
            // You can redirect to an error page or handle it as needed
            return "ERROR";
        }
    }


    @GetMapping("/ViewPage")
    public String showSymptoms(Model model) {
        List<Symptom> symptoms = symptomService.getAllSymptoms();
        model.addAttribute("symptoms", symptoms);
        return "/Admin/Symptom/ViewSymptom"; // Return the name of your Thymeleaf HTML template
    }


    @GetMapping("/DeletePage")
    public String deletePage(){
        return "/Admin/Symptom/DeleteSymptom";
    }
}
