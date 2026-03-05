package com.dashboard.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResetResult", namespace = "http://soap.dashboard.com/")
public class ResetResult {
    
    @XmlElement(required = true)
    private String message;
    
    @XmlElement(required = true)
    private boolean success;
    
    public ResetResult() {
    }
    
    public ResetResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}