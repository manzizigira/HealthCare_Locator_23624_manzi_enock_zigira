package com.auca.exam.HealthCareLocator.Controller;

import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Service.HospitalService;
import com.auca.exam.HealthCareLocator.Service.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/hospitals")
public class HospitalController {
    @Autowired
    private final HospitalService hospitalService;

    @Autowired
    private final SymptomService symptomService;

    @Autowired
    public HospitalController(HospitalService hospitalService, SymptomService symptomService) {
        this.hospitalService = hospitalService;
        this.symptomService = symptomService;
    }

    @GetMapping("/AddPage")
    public String addPage(){
        return "/Admin/Hospital/AddHospital";
    }
     @GetMapping("/DeletePage")
    public String deletePage(){
        return "/Admin/Hospital/DeleteHospital";
    }

    @GetMapping("/OperationalHoursPage")
    public String opPage(){
        return "/Admin/Hospital/OperationalHours";
    }

    @GetMapping("/{hospitalId}")
    public Hospital getHospitalById(@PathVariable Long hospitalId) {
        return hospitalService.getHospitalById(hospitalId)
                .orElseThrow(() -> new NoSuchElementException("Hospital not found"));
    }

    /*@GetMapping("/ViewHospital")
    public String getAll(Model model) {
        List<Hospital>hospitals=hospitalService.listhospita();
        model.addAttribute("hospitals", hospitals);
        return "/Admin/Hospital/ViewHospital"; // Return the view name
    }*/

    @PostMapping("/adde")
    public String addHospital(@ModelAttribute("hospital") Hospital hospitalToUpdate, Model model) {
        hospitalService.addHospital(hospitalToUpdate);
        model.addAttribute("hospitals", hospitalService.getAllHospital());
        return "/Admin/Hospital/ViewHospital";
    }

    @PutMapping("/{hospitalId}")
    public Hospital updateHospital(@PathVariable Long hospitalId, @RequestBody Hospital updatedHospital) {
        return hospitalService.updateHospitalDetails(hospitalId, updatedHospital);
    }

    @DeleteMapping("/{hospitalId}")
    public void deleteHospital(@PathVariable Long hospitalId) {
        hospitalService.deleteHospitalById(hospitalId);
    }

    @GetMapping("/{hospitalId}/operating-hours")
    public String getOperatingHoursOfHospital(@PathVariable Long hospitalId) {
        return hospitalService.getOperatingHoursOfHospital(hospitalId);
    }

    @GetMapping("/nearest")
    public List<Hospital> findNearestHospitals(@RequestParam BigDecimal userLatitude, @RequestParam BigDecimal userLongitude) {
        return hospitalService.findNearestHospitals(userLatitude, userLongitude);
    }

    @GetMapping("/adds")
    public String showAddHospitalForm(Model model) {
        // Fetch all available symptoms from the database
        List<Symptom> allSymptoms = symptomService.getAllSymptoms();

        // Pass the symptoms to the HTML view for populating the select options
        model.addAttribute("allSymptoms", allSymptoms);
        return "AddHospital"; // Your HTML file name
    }

    @PostMapping("/add")
    public String addHospital(@ModelAttribute Hospital hospital,
                              @RequestParam(value = "selectedSymptoms", required = false) List<Long> selectedSymptoms) {
        // Process the hospital data (other fields)

        // Save the hospital first to obtain its ID
        Hospital savedHospital = hospitalService.addHospital(hospital);

        if (selectedSymptoms != null && !selectedSymptoms.isEmpty()) {
            Set<Symptom> symptoms = new HashSet<>();
            for (Long symptomId : selectedSymptoms) {
                Symptom symptom = symptomService.getSymptomById(symptomId);
                symptoms.add(symptom);
            }
            hospital.setTreatedSymptoms(symptoms); // Set the symptoms for the hospital
        }

        Hospital savedHospitall = hospitalService.addHospital(hospital);


        return "/Admin/AdminHome";
    }

    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable String id, Model model){
        Hospital hospital = hospitalService.getHospitalById(Long.parseLong(id)).orElse(null);
        model.addAttribute("hospital", hospital);
        return "/Admin/Hospital/UpdateHospital";
    }

    @GetMapping("/hospital")
    public String getHospitals(Model model) {
        List<Hospital> hospitals = hospitalService.getAllHospital(); // Fetch hospitals from the service

        model.addAttribute("hospitals", hospitals); // Add hospitals to the model

        return "Admin/Hospital/nearestHospital"; // Return the name of the Thymeleaf HTML template
    }


}
