package com.auca.exam.HealthCareLocator.exceptions;

public class HospitalNotFoundException extends RuntimeException{
    public HospitalNotFoundException(String message){
        super(message);
    }
}
