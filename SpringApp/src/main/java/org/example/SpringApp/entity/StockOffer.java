package org.example.SpringApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StockOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;
    private int shares;
    private double pricePerUnit;
    private boolean isOffer;
    private boolean fulfilled;

    public StockOffer() {
    }

    public StockOffer(String type, int shares, double pricePerUnit, boolean isOffer) {
        this.type = type;
        this.shares = shares;
        this.pricePerUnit = pricePerUnit;
        this.isOffer = isOffer;
        this.fulfilled = false;
    }
}