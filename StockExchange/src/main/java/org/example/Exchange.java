package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Exchange {
    public static final List<String> STOCK_TYPES = Arrays.asList("Microsoft", "Apple", "Google", "Amazon", "Facebook");
    private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, List<StockOffer>> stockMarket = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Lock> stockLocks = new ConcurrentHashMap<>();
    private final List<Client> clients;

    public Exchange(List<Client> clients) {
        this.clients = clients;
        List<String> stockTypes = STOCK_TYPES;
        for (String type : stockTypes) {
            stockLocks.put(type, new ReentrantLock());
            stockMarket.put(type, new CopyOnWriteArrayList<>());
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<String> getStockTypes() {
        return STOCK_TYPES;
    }

    public void addStockOffer(StockOffer offer) {
        if (offer.getShares() > 0) {
            String type = offer.getType();
            Lock lock = stockLocks.get(type);
            lock.lock();
            try {
                stockMarket.get(type).add(offer);
                matchOffersAndRequests(type);
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("Ignoring offer with 0 shares");
        }
    }

    public Optional<Client> findClientByOffer(StockOffer offer) {
        for (Client client : clients) {
            Lock clientLock = client.getLock();
            clientLock.lock();
            try {
                if (client.getWallet().contains(offer)) {
                    return Optional.of(client);
                }
            } finally {
                clientLock.unlock();
            }
        }
        return Optional.empty();
    }

    public void matchOffersAndRequests(String type) {
        Lock lock = stockLocks.get(type);

        lock.lock();
        try {
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

                                if (buyer.isPresent() && seller.isPresent() && !buyer.get().getId().equals(seller.get().getId())) {
                                    transactions.add(new Transaction(buyer.get(), seller.get(), offer, sharesExchanged));
                                    System.out.println("Transaction executed: " + buyer.get().getId() + " bought " +
                                            sharesExchanged + " shares of " + offer.getType() +
                                            " from " + seller.get().getId());

                                    matchOffers(matchedOffers, offer, seller);
                                    matchOffers(matchedRequests, request, buyer);
                                }
                            }
                        }
                    }
                }
            }

            offers.removeAll(matchedOffers);
            offers.removeAll(matchedRequests);
        } finally {
            lock.unlock();
        }
    }

    private void matchOffers(List<StockOffer> matchedRequests, StockOffer request, Optional<Client> buyer) {
        if (request.getShares() == 0) {
            matchedRequests.add(request);
            Lock buyerLock = buyer.get().getLock();
            buyerLock.lock();
            try {
                buyer.get().getWallet().remove(request);
            } finally {
                buyerLock.unlock();
            }
        }
    }
}