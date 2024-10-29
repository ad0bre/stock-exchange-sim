package org.example;

import java.util.Objects;

public class StockOffer {
    private String type;
    private int shares;
    private double pricePerUnit;

    public StockOffer(String type, double price, int shares) {
        this.type = type;
        this.pricePerUnit = price;
        this.shares = shares;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOffer stockUnit = (StockOffer) o;
        return Objects.equals(this.type, stockUnit.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public double getPrice() {
        return pricePerUnit;
    }

    public boolean isOffer(StockOffer request){
        return this.type.equals(request.getType()) && this.pricePerUnit <= request.getPrice();
    }
}
