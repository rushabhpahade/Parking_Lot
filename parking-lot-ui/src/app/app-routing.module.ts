// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ParkingComponent } from './parking/parking.component';
import { SearchComponent } from './search/search.component';
import { CarListComponent } from './car-list/car-list.component';
import { ParkedCarListComponent } from './parking-car-list/parking-car-list.component';

const routes: Routes = [
  { path: 'parking', component: ParkingComponent },
  { path: 'search', component: SearchComponent },
  { path: 'car-list', component: CarListComponent },
  { path: 'parked-car-list', component: ParkedCarListComponent },
  { path: '', redirectTo: '/parking', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
