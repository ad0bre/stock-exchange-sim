package org.example.SpringApp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StockOffer> wallet = new ArrayList<>();

    public Client() {
    }

    public Client(String name, List<StockOffer> wallet) {
        this.name = name;
        this.wallet = wallet;
    }
}