package org.example.SpringApp.repository;

import org.example.SpringApp.entity.StockOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockOfferRepository extends JpaRepository<StockOffer, Long> {}