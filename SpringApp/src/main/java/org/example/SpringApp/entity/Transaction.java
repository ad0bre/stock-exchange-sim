package org.example.SpringApp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Client buyer;

    @ManyToOne
    private Client seller;

    @ManyToOne
    private StockOffer offer;

    private int exchangedShares;

    public Transaction() {
    }

    public Transaction(Client buyer, Client seller, StockOffer offer, int exchangedShares) {
        this.buyer = buyer;
        this.seller = seller;
        this.offer = offer;
        this.exchangedShares = exchangedShares;
    }
}