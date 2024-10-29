package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Client implements Runnable{
    private final UUID id;
    private ArrayList<StockOffer> wallet;
    private final Random random = new Random();

    public Client(){
        this.id = UUID.randomUUID();
        this.wallet = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void generateWallet(ArrayList<String> types){
        for (String type : types){
            var numberOfShares = random.nextInt(25);
            for (int i = 0; i < numberOfShares; i++) {
                this.wallet.add(
                        new StockOffer(
                                type,
                                random.nextDouble(10),
                                random.nextInt(50),
                                random.nextBoolean()));
            }
        }
    }

    public ArrayList<StockOffer> getWallet() {
        return wallet;
    }

    public StockOffer createOffer(String type){
        return new StockOffer(
                type,
                random.nextDouble(10),
                random.nextInt(50),
                true
        );
    }

    public StockOffer createRequest(String type){
        return new StockOffer(
                type,
                random.nextDouble(10),
                random.nextInt(50),
                false
        );
    }

    @Override
    public void run() {
        System.out.println("Client " + this.id + " is running...");
    }
}
