package com.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private ObjectId id;
    private String idStr;
    private String name;
    private String email;
    private String phone;
    private String address;
    private double purchaseAmount;
    private int purchaseCount;
    private String registrationDate;
    private String lastPurchaseDate;
    private boolean active;
}