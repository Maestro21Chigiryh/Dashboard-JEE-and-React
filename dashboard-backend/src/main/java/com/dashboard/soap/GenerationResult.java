package com.dashboard.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenerationResult", namespace = "http://soap.dashboard.com/")
public class GenerationResult {
    
    @XmlElement(required = true)
    private String message;
    
    @XmlElement(required = true)
    private long count;
    
    public GenerationResult() {
    }
    
    public GenerationResult(String message, long count) {
        this.message = message;
        this.count = count;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getCount() {
        return count;
    }
    
    public void setCount(long count) {
        this.count = count;
    }
}