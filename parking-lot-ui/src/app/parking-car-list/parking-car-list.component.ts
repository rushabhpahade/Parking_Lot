import { Component, OnInit } from '@angular/core';
import { CarService } from '../car.service';

@Component({
  selector: 'app-car-list',
  templateUrl: './parking-car-list.component.html',
  styleUrls: ['./parking-car-list.component.css']
})
export class ParkedCarListComponent implements OnInit {
  cars: any[] = [];;

  constructor(private carService: CarService) {}

  ngOnInit() {
    this.carService.getParkedCarsList().subscribe(response => {
      this.cars = response as any[];
    }, error => {
      console.error('Error fetching car list:', error);
    });
  }
}
