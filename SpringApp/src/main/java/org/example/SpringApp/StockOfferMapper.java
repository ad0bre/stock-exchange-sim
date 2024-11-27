package org.example.SpringApp;

import org.example.SpringApp.entity.StockOffer;
import org.example.SpringApp.entity.Transaction;
import org.example.SpringApp.entity.Client;

import java.util.stream.Collectors;

public class StockOfferMapper {

    public static Transaction toEntity(org.example.Transaction pureTransaction) {
        Transaction transactionEntity = new Transaction();

        transactionEntity.setBuyer(toEntity(pureTransaction.getBuyer()));
        transactionEntity.setSeller(toEntity(pureTransaction.getSeller()));

        transactionEntity.setOffer(toEntity(pureTransaction.getOffer()));

        transactionEntity.setExchangedShares(pureTransaction.getExchangedShares());

        return transactionEntity;
    }

    public static StockOffer toEntity(org.example.StockOffer pureStockOffer) {
        return new StockOffer(
            pureStockOffer.getType(),
            pureStockOffer.getShares(),
            pureStockOffer.getPrice(),
            pureStockOffer.isOffer()
        );
    }

    public static org.example.StockOffer toPure(StockOffer stockOffer) {
        return new org.example.StockOffer(
            stockOffer.getType(),
            stockOffer.getPricePerUnit(),
            stockOffer.getShares(),
            stockOffer.isOffer()
        );
    }

    public static Client toEntity(org.example.Client pureClient) {
        Client clientEntity = new Client();
        clientEntity.setName("Client_" + pureClient.getId());
        clientEntity.setWallet(
                pureClient.getWallet().stream()
                        .map(StockOfferMapper::toEntity)
                        .collect(Collectors.toList())
        );
        return clientEntity;
    }

    public static org.example.Client toPureClient(Client clientEntity, org.example.Exchange exchange) {
        org.example.Client pureClient = new org.example.Client(exchange);
        pureClient.getWallet().addAll(
                clientEntity.getWallet().stream()
                        .map(StockOfferMapper::toPure)
                        .collect(Collectors.toList())
        );
        return pureClient;
    }
}