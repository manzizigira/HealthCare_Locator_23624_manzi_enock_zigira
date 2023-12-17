package com.auca.exam.HealthCareLocator.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

@Entity
@Table(name = "symptoms")
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "treatedSymptoms")
    @NotNull
    private Set<Hospital> hospitals = new HashSet<>();

    public Symptom() {
    }

    public Symptom(Long id, String name, Set<Hospital> hospitals) {
        this.id = id;
        this.name = name;
        this.hospitals = hospitals;
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

    public Set<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(Set<Hospital> hospitals) {
        this.hospitals = hospitals;
    }
}
