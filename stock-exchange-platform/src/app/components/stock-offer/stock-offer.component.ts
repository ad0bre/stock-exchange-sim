import { Component } from '@angular/core';
import { StockOfferService } from '../../services/stock-offer.service';
import { FormsModule } from '@angular/forms';
import { NgForOf } from '@angular/common';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatList, MatListItem } from '@angular/material/list';
import { MatCard } from '@angular/material/card';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import {MatCheckbox} from '@angular/material/checkbox';

@Component({
  selector: 'app-stock-offer',
  templateUrl: './stock-offer.component.html',
  imports: [
    FormsModule,
    NgForOf,
    MatFormField,
    MatListItem,
    MatList,
    MatCard,
    MatInput,
    MatLabel,
    MatButton,
    MatCheckbox
  ],
  styleUrls: ['./stock-offer.component.css']
})
export class StockOfferComponent {
  stockOffers: any[] = [
    { id: 1, type: 'Buy', shares: 100, pricePerUnit: 50, offer: true },
    { id: 2, type: 'Sell', shares: 200, pricePerUnit: 75, offer: true },
    { id: 3, type: 'Buy', shares: 150, pricePerUnit: 60, offer: true }
  ];
  newOffer = { type: '', shares: 0, pricePerUnit: 0, offer: true };

  constructor(private stockOfferService: StockOfferService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.fetchStockOffers();
  }

  fetchStockOffers() {
    this.stockOfferService.getAllOffers().subscribe((data) => {
      this.stockOffers = data;
    });
  }

  createOffer() {
    this.stockOfferService.createStockOffer(this.newOffer).subscribe({
      next: () => {
        this.fetchStockOffers();
        this.snackBar.open('Offer created successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        alert('Failed to create offer: ' + error.message);
      }
    });
  }

  processOffers() {
    this.stockOfferService.processStockOffers().subscribe({
      next: () => {
        this.fetchStockOffers();
        this.snackBar.open('Offers processed successfully', 'Close', { duration: 3000 });
      },
      error: (error) => {
        alert('Failed to process offers: ' + error.message);
      }
    });
  }
}
