package org.example.SpringApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.SpringApp.entity.StockOffer;
import org.example.SpringApp.entity.Transaction;
import org.example.SpringApp.service.StockOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-offers")
@Tag(name = "Stock Offers", description = "API for managing stock offers and transactions")
public class StockOfferController {

    @Autowired
    private StockOfferService stockOfferService;

    @PostMapping
    @Operation(summary = "Create a stock offer", description = "Create a new stock offer in the system")
    public ResponseEntity<StockOffer> createStockOffer(@RequestBody StockOffer offer) {
        StockOffer savedOffer = stockOfferService.createStockOffer(offer);
        return ResponseEntity.ok(savedOffer);
    }

    @GetMapping
    @Operation(summary = "Get all stock offers", description = "Retrieve a list of all stock offers")
    public ResponseEntity<List<StockOffer>> getAllStockOffers() {
        return ResponseEntity.ok(stockOfferService.getAllStockOffers());
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transactions")
    public ResponseEntity<List<Transaction>> getTransactions() {
        return ResponseEntity.ok(stockOfferService.getTransactions());
    }

    @PostMapping("/process")
    @Operation(summary = "Process stock offers", description = "Process stock offers and match them with requests")
    public ResponseEntity<Void> processOffersAndRequests() {
        stockOfferService.processOffersAndRequests();
        return ResponseEntity.ok().build();
    }
}