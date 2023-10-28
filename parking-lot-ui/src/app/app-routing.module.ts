// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ParkingComponent } from './parking/parking.component';
import { SearchComponent } from './search/search.component';
import { CarListComponent } from './car-list/car-list.component';

const routes: Routes = [
  { path: 'parking', component: ParkingComponent },
  { path: 'search', component: SearchComponent },
  { path: 'car-list', component: CarListComponent },
  { path: '', redirectTo: '/parking', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
