import {Component, OnInit, ViewChild, AfterViewInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {StockOfferService} from '../../services/stock-offer.service';
import {CommonModule} from '@angular/common';
import {MatCard} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css'],
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCard, MatPaginatorModule]
})
export class TransactionsComponent implements OnInit, AfterViewInit {
  transactions: any[] = [];
  displayedColumns: string[] = ['buyer', 'seller', 'shares'];
  dataSource = new MatTableDataSource(this.transactions);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private stockOfferService: StockOfferService) {}

  ngOnInit() {
    this.stockOfferService.getAllTransactions().subscribe((data: any[]) => {
      this.transactions = data;
      this.dataSource.data = this.transactions;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }
}
