package org.example;

import java.util.UUID;

public class Transaction {
    private String id;
    private Client buyer;
    private Client seller;
    private StockOffer offer;
    private int exchangedShares;

    public Transaction(Client buyer, Client seller, StockOffer offer, int exchangedShares) {
        this.exchangedShares = exchangedShares;
        this.id = IdGenerator.generate();
        this.buyer = buyer;
        this.seller = seller;
        this.offer = offer;
    }

    @Override
    public String toString(){
        return "New transaction " + this.id +
                ": buyer " + this.buyer.getId() +
                " bought "+ this.exchangedShares +
                " shares of " + this.offer.getType() +
                " from seller " + this.seller.getId() +
                " for " + this.offer.getPrice() + " per unit.";
    }
}
