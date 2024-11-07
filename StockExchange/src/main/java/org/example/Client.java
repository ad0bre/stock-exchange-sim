package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Client implements Runnable {

    private final UUID id;
    private final ArrayList<StockOffer> wallet;
    private final Random random = new Random();
    private final Exchange exchange;
    private final CopyOnWriteArrayList<String> types = new CopyOnWriteArrayList<>(
            Arrays.asList("Microsoft", "Apple", "Google", "Amazon", "Facebook"));

    public Client(Exchange exchange) {
        this.exchange = exchange;
        this.id = UUID.randomUUID();
        this.wallet = new ArrayList<>();
        generateWallet(types);
    }

    public UUID getId() {
        return id;
    }

    public synchronized ArrayList<StockOffer> getWallet() {
        return wallet;
    }

    public void generateWallet(CopyOnWriteArrayList<String> types) {
        for (String type : types) {
            int numberOfShares = random.nextInt(10);
            for (int i = 0; i < numberOfShares; i++) {
                this.wallet.add(
                        new StockOffer(
                                type,
                                random.nextInt(10),
                                random.nextInt(10) + 1,
                                random.nextBoolean()));
            }
        }
    }

    public synchronized StockOffer createOffer(String type) {
        StockOffer offer = new StockOffer(
                type,
                random.nextInt(10),
                random.nextInt(10) + 1,
                true
        );
        wallet.add(offer);  // Add the offer to the wallet
        System.out.println("Seller " + this.id +
                " wants to sell " + offer.getShares() +
                " shares of " + offer.getType() +
                " for " + offer.getPrice() + " per unit.");
        return offer;
    }

    public synchronized StockOffer createRequest(String type) {
        StockOffer request = new StockOffer(
                type,
                random.nextInt(10),
                random.nextInt(10) + 1,
                false
        );
        wallet.add(request);  // Add the request to the wallet
        System.out.println("Buyer " + this.id +
                " wants to buy " + request.getShares() +
                " shares of " + request.getType() +
                " for " + request.getPrice() + " per unit.");
        return request;
    }

    @Override
    public void run() {
        while (true) {
            String type = types.get(random.nextInt(types.size()));
            StockOffer stockOffer = random.nextBoolean() ? createOffer(type) : createRequest(type);

            exchange.addStockOffer(stockOffer);

            try {
                Thread.sleep(random.nextInt(3000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}