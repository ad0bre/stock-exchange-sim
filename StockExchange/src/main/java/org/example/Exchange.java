package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Exchange implements Runnable{
    private CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<>();
    private ConcurrentHashMap<String, CopyOnWriteArrayList<StockOffer>> stockMarket = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<String> types;
    private int numberOfClients;

    public Exchange(int numberOfClients){
        this.numberOfClients = numberOfClients;
        types = new CopyOnWriteArrayList<String>(Arrays.asList("Microsoft", "Apple", "Google", "Amazon", "Facebook"));
    }

    @Override
    public void run() {
        for (String type : types){
            stockMarket.put(type, new CopyOnWriteArrayList<StockOffer>());
        }

        for (int i = 0; i < numberOfClients; i++){
            clients.add(new Client(types));
        }

        for (Client client : clients){
            for (StockOffer offer : client.getWallet()){
                stockMarket.get(offer.getType()).add(offer);
            }
        }

        for (Client client : clients){
            for (String type : types){
                StockOffer request = client.createRequest(type);
                for (StockOffer offer : stockMarket.get(type)){
                    if (offer.match(request)){
                        if (offer.getLock().tryLock()){
                            if (request.getLock().tryLock()){
                                Optional<Client> optional = clients.stream().filter(c -> c.getWallet().contains(offer)).findFirst();
                                if (optional.isEmpty()){
                                    System.out.println("No seller found for offer " + offer);
                                }
                                else {
                                    Client seller = optional.get();
                                    if (offer.getShares() > 0 && request.getShares() > 0){
                                        offer.exchangeShares(request);
                                        transactions.add(new Transaction(client, seller, offer, request.getShares()));
                                    }
                                }
                                request.getLock().unlock();
                            }
                            offer.getLock().unlock();
                        }
                    }
                }
            }
        }
    }
}
