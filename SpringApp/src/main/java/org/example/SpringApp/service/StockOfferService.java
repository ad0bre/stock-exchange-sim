package org.example.SpringApp.service;

import org.example.SpringApp.StockOfferMapper;
import org.example.SpringApp.entity.Client;
import org.example.SpringApp.entity.StockOffer;
import org.example.SpringApp.entity.Transaction;
import org.example.SpringApp.repository.StockOfferRepository;
import org.example.SpringApp.repository.TransactionRepository;
import org.example.SpringApp.repository.ClientRepository;
import org.example.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class StockOfferService {

    @Autowired
    private StockOfferRepository stockOfferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    private final List<StockOffer> stockOffers = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();

    @PostConstruct
    public void initStockOffers() {
        stockOffers.addAll(stockOfferRepository.findAll());
    }

    @Scheduled(fixedDelay = 500)
    public void processContinuously() {
        processOffersAndRequests();
    }

    @PostConstruct
    public void initDatabase() {
        if (clientRepository.count() == 0) {
            for (int i = 1; i <= 5; i++) {
                Client client = new Client();
                client.setName("Client_" + i);

                List<StockOffer> wallet = new ArrayList<>();
                for (int j = 0; j < 5; j++) {
                    StockOffer offer = new StockOffer(
                            getRandomStockType(),
                            (int) (Math.random() * 10) + 1,
                            Math.round((Math.random() * 100 + 1) * 100.0) / 100.0,
                            Math.random() > 0.5
                    );
                    wallet.add(offer);
                }
                client.setWallet(wallet);

                clientRepository.save(client);
            }
        }
    }

    private String getRandomStockType() {
        List<String> stockTypes = List.of("Microsoft", "Apple", "Google", "Amazon", "Facebook");
        return stockTypes.get((int) (Math.random() * stockTypes.size()));
    }

    public StockOffer createStockOffer(StockOffer offer) {
        lock.lock();
        initStockOffers();
        try {
            StockOffer savedOffer = stockOfferRepository.save(offer);

            List<Client> clients = clientRepository.findAll();
            if (clients.isEmpty()) {
                throw new RuntimeException("No clients found to assign the stock offer.");
            }

            Client randomClient = clients.get((int) (Math.random() * clients.size()));

            randomClient.getWallet().add(savedOffer);

            clientRepository.save(randomClient);

            stockOffers.add(savedOffer);

            System.out.println("Assigned StockOffer ID " + savedOffer.getId() + " to Client ID " + randomClient.getId());
            return savedOffer;
        } finally {
            lock.unlock();
        }
    }

    public List<StockOffer> getAllStockOffers() {
        return stockOfferRepository.findAll();
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public void processOffersAndRequests() {
        lock.lock();
        try {
            List<StockOffer> matchedOffers = new ArrayList<>();
            List<StockOffer> matchedRequests = new ArrayList<>();

            for (StockOffer offer : stockOffers) {
                if (offer.isOffer() && offer.getShares() > 0) {
                    for (StockOffer request : stockOffers) {
                        if (!request.isOffer() && request.getShares() > 0 && offer != request && match(offer, request)) {
                            int sharesBeforeExchange = offer.getShares() + request.getShares();
                            exchangeShares(offer, request);
                            int sharesExchanged = sharesBeforeExchange - (offer.getShares() + request.getShares());

                            if (sharesExchanged > 0) {
                                Client buyer = findClientByOffer(request);
                                Client seller = findClientByOffer(offer);

                                Transaction transaction = new Transaction(buyer, seller, offer, sharesExchanged);
                                transactionRepository.save(transaction);

                                matchedOffers.add(offer);
                                matchedRequests.add(request);

                                System.out.println("Transaction executed: Buyer " + buyer.getName() +
                                        " bought " + sharesExchanged +
                                        " shares of " + offer.getType() +
                                        " from Seller " + seller.getName());
                            }
                        }
                    }
                }
            }

            stockOffers.removeAll(matchedOffers);
            stockOffers.removeAll(matchedRequests);
        } finally {
            lock.unlock();
        }
    }

    private boolean match(StockOffer offer, StockOffer request) {
        return offer.getType().equals(request.getType()) &&
                Math.abs(offer.getPricePerUnit() - request.getPricePerUnit()) < 1.0;
    }

    private void exchangeShares(StockOffer offer, StockOffer request) {
        int exchangedShares = Math.min(offer.getShares(), request.getShares());
        offer.setShares(offer.getShares() - exchangedShares);
        request.setShares(request.getShares() - exchangedShares);
    }

    private Client findClientByOffer(StockOffer offer) {
        return clientRepository.findAll().stream()
                .filter(client -> client.getWallet().stream()
                        .anyMatch(walletOffer -> walletOffer.getId().equals(offer.getId())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Client not found for offer: " + offer));
    }
}