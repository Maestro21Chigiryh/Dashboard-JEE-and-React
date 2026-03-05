// Transaction.java
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
public class Transaction {
    private ObjectId id;
    private ObjectId clientId;
    private double amount;
    private String date;
    private String productName;
    private int quantity;
}