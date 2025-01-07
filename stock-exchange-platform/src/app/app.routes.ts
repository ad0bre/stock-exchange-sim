import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { StockOfferComponent } from './components/stock-offer/stock-offer.component';
import { TransactionsComponent } from './components/transactions/transactions.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent }, // Default route
  { path: 'stock-offers', component: StockOfferComponent }, // Stock offers route
  { path: 'transactions', component: TransactionsComponent }, // Transactions route
  { path: '**', redirectTo: '/' }, // Wildcard route redirects to the dashboard
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
