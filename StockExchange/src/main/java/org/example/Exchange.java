package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Exchange {
    private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, List<StockOffer>> stockMarket = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> stockMonitors = new ConcurrentHashMap<>();
    private final List<Client> clients;

    public Exchange(List<Client> clients) {
        this.clients = clients;
        List<String> stockTypes = Arrays.asList("Microsoft", "Apple", "Google", "Amazon", "Facebook");
        for (String type : stockTypes) {
            stockMonitors.put(type, new Object());
            stockMarket.put(type, new CopyOnWriteArrayList<>());
        }
    }

    public void addStockOffer(StockOffer offer) {
        if (offer.getShares() > 0) {
            String type = offer.getType();
            Object monitor = stockMonitors.get(type);

            synchronized (monitor) {
                stockMarket.get(type).add(offer);
                matchOffersAndRequests(type);
                monitor.notifyAll();
            }
        } else {
            System.out.println("ignoring offer with 0 shares");
        }
    }

    public Optional<Client> findClientByOffer(StockOffer offer) {
        for (Client client : clients) {
            synchronized (client) {
                if (client.getWallet().contains(offer)) {
                    return Optional.of(client);
                }
            }
        }
        return Optional.empty();
    }

    private void matchOffersAndRequests(String type) {
        Object monitor = stockMonitors.get(type);

        synchronized (monitor) {
            List<StockOffer> offers = stockMarket.get(type);
            List<StockOffer> matchedOffers = new ArrayList<>();
            List<StockOffer> matchedRequests = new ArrayList<>();

            for (StockOffer offer : offers) {
                if (offer.isOffer() && offer.getShares() > 0) {
                    for (StockOffer request : offers) {
                        if (!request.isOffer() && request.getShares() > 0 && offer != request && offer.match(request)) {
                            int sharesBeforeExchange = offer.getShares() + request.getShares();
                            offer.exchangeShares(request);
                            int sharesExchanged = sharesBeforeExchange - (offer.getShares() + request.getShares());

                            if (sharesExchanged > 0) {
                                Optional<Client> buyer = findClientByOffer(request);
                                Optional<Client> seller = findClientByOffer(offer);

                                if (buyer.isPresent() && seller.isPresent()) {
                                    transactions.add(new Transaction(buyer.get(), seller.get(), offer, sharesExchanged));
                                    System.out.println("Transaction executed: " + buyer.get().getId() + " bought " +
                                            sharesExchanged + " shares of " + offer.getType() +
                                            " from " + seller.get().getId());

                                    if (offer.getShares() == 0) {
                                        matchedOffers.add(offer);
                                        synchronized (seller.get()) {
                                            seller.get().getWallet().remove(offer);
                                        }
                                    }
                                    if (request.getShares() == 0) {
                                        matchedRequests.add(request);
                                        synchronized (buyer.get()) {
                                            buyer.get().getWallet().remove(request);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            offers.removeAll(matchedOffers);
            offers.removeAll(matchedRequests);

            monitor.notifyAll();
        }
    }
}