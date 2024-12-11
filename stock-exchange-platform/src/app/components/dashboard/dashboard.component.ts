import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatGridList, MatGridTile} from '@angular/material/grid-list';
import {MatAnchor} from '@angular/material/button';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [
    RouterLinkActive,
    RouterLink,
    MatCardContent,
    MatCard,
    MatGridList,
    MatGridTile,
    MatCardTitle,
    MatCardHeader,
    MatAnchor
  ]
})
export class DashboardComponent {}
