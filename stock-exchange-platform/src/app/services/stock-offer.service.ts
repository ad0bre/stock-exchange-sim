import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockOfferService {
  private baseUrl = 'http://localhost:8080/api/stock-offers';

  constructor(private http: HttpClient) {}

  getAllOffers(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  createStockOffer(stockOffer: any): Observable<any> {
    return this.http.post(`${this.baseUrl}`, stockOffer);
  }

  processStockOffers(): Observable<any> {
    return this.http.post(`${this.baseUrl}/process`, {});
  }

  getAllTransactions(): Observable<any> {
    return this.http.get(`${this.baseUrl}/transactions`);
  }
}
