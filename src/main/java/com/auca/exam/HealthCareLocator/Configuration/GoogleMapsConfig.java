package com.auca.exam.HealthCareLocator.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GoogleMapsConfig {
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }
}
