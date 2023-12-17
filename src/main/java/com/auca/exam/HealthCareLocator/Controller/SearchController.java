package com.auca.exam.HealthCareLocator.Controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/Search")
public class SearchController {
    private static final String[] SEARCH_DIRECTORIES = {
            "Admin", "Admin/Hospital", "Admin/Symptom", "Admin/Users", "Client"
    };

    private static final String[] PAGES = {
            "hospital", "symptom", "users"
    };
    private static final String TEMPLATE_PREFIX = "templates/";

    @GetMapping("/results")
    public String search(@RequestParam("query") String query) {
        return "redirect:/goToPage?query=" + query;
    }

    @GetMapping("/suggestions")
    @ResponseBody
    public List<String> getSuggestions(@RequestParam("query") String query) {
        return getMatchingPageNames(query);
    }

    @GetMapping("/goToPage")
    public String goToPage(@RequestParam("query") String query) {
        for (String page : PAGES) {
            if (query.equalsIgnoreCase(page)) {
                return TEMPLATE_PREFIX + "Admin/" + page; // Adjust this path based on your template structure
            }
        }
        // If the query doesn't match any predefined pages, redirect to a default page or handle the case accordingly
        return "redirect:/"; // For example, redirect to the root if no match is found
    }


    private List<String> getMatchingPageNames(String query) {
        List<String> suggestions = new ArrayList<>();

        if (query.isEmpty()) { // Return all directories if the query is empty
            suggestions.addAll(Arrays.asList(SEARCH_DIRECTORIES));
        } else {
            for (String directory : SEARCH_DIRECTORIES) {
                if (directory.toLowerCase().contains(query.toLowerCase())) {
                    suggestions.add(directory);
                }
            }
        }

        return suggestions;
    }
}
