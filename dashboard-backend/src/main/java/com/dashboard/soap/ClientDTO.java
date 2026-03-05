package com.dashboard.soap;

import com.dashboard.model.Client;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClientDTO", namespace = "http://soap.dashboard.com/")
public class ClientDTO {
    
    @XmlElement(required = true)
    private String id;
    
    @XmlElement(required = true)
    private String name;
    
    @XmlElement(required = true)
    private String email;
    
    @XmlElement(required = false)
    private String phone;
    
    @XmlElement(required = false)
    private String address;
    
    @XmlElement(required = true)
    private double purchaseAmount;
    
    @XmlElement(required = true)
    private int purchaseCount;
    
    @XmlElement(required = true)
    private String registrationDate;
    
    @XmlElement(required = true)
    private String lastPurchaseDate;
    
    @XmlElement(required = true)
    private boolean active;
    
    // Constructeur par défaut requis par JAXB
    public ClientDTO() {
    }
    
    // Constructeur de conversion depuis l'entité Client
    public ClientDTO(Client client) {
        this.id = client.getId().toString();
        this.name = client.getName();
        this.email = client.getEmail();
        this.phone = client.getPhone();
        this.address = client.getAddress();
        this.purchaseAmount = client.getPurchaseAmount();
        this.purchaseCount = client.getPurchaseCount();
        this.registrationDate = client.getRegistrationDate();
        this.lastPurchaseDate = client.getLastPurchaseDate();
        this.active = client.isActive();
    }
    
    // Getters et setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public double getPurchaseAmount() {
        return purchaseAmount;
    }
    
    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
    
    public int getPurchaseCount() {
        return purchaseCount;
    }
    
    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
    
    public String getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }
    
    public void setLastPurchaseDate(String lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}