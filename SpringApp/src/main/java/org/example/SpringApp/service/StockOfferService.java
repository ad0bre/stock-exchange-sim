package org.example.SpringApp.service;

import org.example.SpringApp.StockOfferMapper;
import org.example.SpringApp.entity.StockOffer;
import org.example.SpringApp.entity.Transaction;
import org.example.SpringApp.repository.StockOfferRepository;
import org.example.SpringApp.repository.TransactionRepository;
import org.example.SpringApp.repository.ClientRepository;
import org.example.SpringApp.StockOfferMapper;
import org.example.Exchange;
import org.example.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class StockOfferService {

    @Autowired
    private StockOfferRepository stockOfferRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Exchange exchange;

    @PostConstruct
    public void initExchange() {
        List<org.example.SpringApp.entity.Client> clientEntities = clientRepository.findAll();

        List<org.example.Client> pureClients = clientEntities.stream()
                .map(clientEntity -> StockOfferMapper.toPureClient(clientEntity, exchange))
                .toList();

        exchange = new Exchange(pureClients);
    }

    public StockOffer createStockOffer(StockOffer offerEntity) {
        org.example.StockOffer pureOffer = StockOfferMapper.toPure(offerEntity);

        exchange.addStockOffer(pureOffer);

        return stockOfferRepository.save(offerEntity);
    }

    public List<StockOffer> getAllStockOffers() {
        return stockOfferRepository.findAll();
    }

    public List<Transaction> getTransactions() {
        List<org.example.Transaction> pureTransactions = exchange.getTransactions();

        pureTransactions.forEach(pureTransaction -> {
            Transaction transactionEntity = StockOfferMapper.toEntity(pureTransaction);
            transactionRepository.save(transactionEntity);
        });

        return transactionRepository.findAll();
    }

    public void processOffersAndRequests() {
        for (String type : exchange.getStockTypes()) {
            exchange.matchOffersAndRequests(type);
        }
    }
}