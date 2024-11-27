package org.example.SpringApp.controller;

import org.example.SpringApp.entity.StockOffer;
import org.example.SpringApp.entity.Transaction;
import org.example.SpringApp.service.StockOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-offers")
public class StockOfferController {
    @Autowired
    private StockOfferService stockOfferService;

    @PostMapping
    public ResponseEntity<StockOffer> createStockOffer(@RequestBody StockOffer offer) {
        StockOffer savedOffer = stockOfferService.createStockOffer(offer);
        return ResponseEntity.ok(savedOffer);
    }

    @GetMapping
    public ResponseEntity<List<StockOffer>> getAllStockOffers() {
        return ResponseEntity.ok(stockOfferService.getAllStockOffers());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(stockOfferService.getTransactions());
    }
}