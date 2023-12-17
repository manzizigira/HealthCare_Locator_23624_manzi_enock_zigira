package com.auca.exam.HealthCareLocator.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.*;

@Entity
@Table(name = "hospitals")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private BigDecimal latitude;
    @NotNull
    private BigDecimal longitude;

    @Column(name = "google_maps_url") // Adding a column for Google Maps URL in the database
    private String googleMapsUrl;

    @ElementCollection
    @CollectionTable(name = "hospital_hours", joinColumns = @JoinColumn(name = "hospital_id"))
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "operating_hours")
    @NotNull
    private Map<String, String> operatingHours = new HashMap<>();

    @JsonIgnore
    @ManyToMany
    @NotNull
    @JoinTable(
            name = "hospital_treated_symptoms",
            joinColumns = @JoinColumn(name = "hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "symptom_id")
    )
    private Set<Symptom> treatedSymptoms = new HashSet<>();

    public Hospital() {
    }

    public Hospital(Long id, String name, String address, BigDecimal latitude, BigDecimal longitude, String googleMapsUrl, Map<String, String> operatingHours, Set<Symptom> treatedSymptoms) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.googleMapsUrl = googleMapsUrl;
        this.operatingHours = operatingHours;
        this.treatedSymptoms = treatedSymptoms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getGoogleMapsUrl() {
        return googleMapsUrl;
    }

    public void setGoogleMapsUrl(String googleMapsUrl) {
        this.googleMapsUrl = googleMapsUrl;
    }

    public Map<String, String> getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(Map<String, String> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public Set<Symptom> getTreatedSymptoms() {
        return treatedSymptoms;
    }

    public void setTreatedSymptoms(Set<Symptom> treatedSymptoms) {
        this.treatedSymptoms = treatedSymptoms;
    }
}
