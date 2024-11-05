package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client implements Runnable{
    private final UUID id;
    private ArrayList<StockOffer> wallet;
    private final Random random = new Random();

    public Client(CopyOnWriteArrayList<String> types){
        this.id = UUID.randomUUID();
        this.wallet = new ArrayList<>();
        generateWallet(types);
    }

    public UUID getId() {
        return id;
    }

    public void generateWallet(CopyOnWriteArrayList<String> types){
        for (String type : types){
            var numberOfShares = random.nextInt(25);
            for (int i = 0; i < numberOfShares; i++) {
                this.wallet.add(
                        new StockOffer(
                                type,
                                random.nextDouble(10),
                                random.nextInt(50) + 1,
                                random.nextBoolean()));
            }
        }
    }

    public ArrayList<StockOffer> getWallet() {
        return wallet;
    }

    public StockOffer createOffer(String type){
        StockOffer offer =  new StockOffer(
                type,
                random.nextDouble(10),
                random.nextInt(50),
                true
        );

        System.out.println("Seller " + this.id +
                " wants to sell " + offer.getShares() +
                " shares of " + offer.getType() +
                " for " + offer.getPrice() + " per unit.");

        return offer;
    }

    public StockOffer createRequest(String type){
        StockOffer request = new StockOffer(
                type,
                random.nextDouble(10),
                random.nextInt(50),
                false
        );

        System.out.println("Buyer " + this.id +
                " wants to buy " + request.getShares() +
                " shares of " + request.getType() +
                " for " + request.getPrice() + " per unit.");

        return request;
    }

    @Override
    public void run() {

    }
}
