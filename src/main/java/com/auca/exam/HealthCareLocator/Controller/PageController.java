package com.auca.exam.HealthCareLocator.Controller;

import com.auca.exam.HealthCareLocator.Model.Hospital;
import com.auca.exam.HealthCareLocator.Model.Symptom;
import com.auca.exam.HealthCareLocator.Service.HospitalService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class PageController {


    @Autowired
    private HospitalService hospitalService;


    @GetMapping("/p")
    public String viewListAllHospitals(Model model, @RequestParam(defaultValue = "") String searchTerm) {
        /*model.addAttribute("hospitals", hospitalService.getAllHospital());
        */return listHospitalPaginated(1, "id", "asc", searchTerm, model);
    }


    @GetMapping("/downloadCSV")
    public void downloadCSV(HttpServletResponse response) {
        try {
            // Set response content type
            response.setContentType("text/csv");

            // Set header for the CSV attachment
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=hospital.csv";
            response.setHeader(headerKey, headerValue);

            // Get the table data
            List<Hospital> hospitalList = hospitalService.listhospita();

            // Create a StringBuilder to store CSV content
            StringBuilder csvContent = new StringBuilder();

            // Write header
            csvContent.append("ID, Name, Address, Latitude, Longitude, Google Maps URL, Operating Hours, Treated Symptoms\n");

            // Write data
            for (Hospital hospital : hospitalList) {
                // Append hospital details
                csvContent.append(hospital.getId()).append(",");
                csvContent.append(hospital.getName()).append(",");
                csvContent.append(hospital.getAddress()).append(",");
                csvContent.append(hospital.getLatitude()).append(",");
                csvContent.append(hospital.getLongitude()).append(",");
                csvContent.append(hospital.getGoogleMapsUrl()).append(",");

                // Append Operating Hours
                Map<String, String> operatingHours = hospital.getOperatingHours();
                StringBuilder operatingHoursStr = new StringBuilder();
                for (Map.Entry<String, String> entry : operatingHours.entrySet()) {
                    operatingHoursStr.append(entry.getKey()).append(": ").append(entry.getValue()).append(" | ");
                }
                csvContent.append(operatingHoursStr).append(",");

                // Append Treated Symptoms
                Set<Symptom> treatedSymptoms = hospital.getTreatedSymptoms();
                StringBuilder treatedSymptomsStr = new StringBuilder();
                for (Symptom symptom : treatedSymptoms) {
                    treatedSymptomsStr.append(symptom.getName()).append(", ");
                }
                csvContent.append(treatedSymptomsStr).append("\n");
            }

            // Write CSV content to the response output stream
            response.getWriter().write(csvContent.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @GetMapping(value = "/ViewHospital/{pageNo}")
    public String listHospitalPaginated(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortDir, @RequestParam(defaultValue = "") String searchTerm, Model model) {

        int pageSize = 5;
        Page<Hospital> page = hospitalService.listHospitalsPaginated(pageNo, pageSize, sortField, sortDir, searchTerm);
        List<Hospital> hospitals = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("Hospitals", hospitals);
        model.addAttribute("searchTerm", searchTerm);

        return "/Admin/Hospital/ViewHospital";
    }


}
