package org.example;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockOffer {
    private UUID id;
    private String type;
    private int shares;
    private double pricePerUnit;
    private boolean isOffer;

    private final Lock lock = new ReentrantLock();

    public StockOffer(String type, double price, int shares, boolean isOffer) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.pricePerUnit = price;
        this.shares = shares;
        this.isOffer = isOffer;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOffer that = (StockOffer) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public double getPrice() {
        return pricePerUnit;
    }

    public boolean match(StockOffer request){
        return this.type.equals(request.getType()) && this.pricePerUnit == request.getPrice();
    }
}
