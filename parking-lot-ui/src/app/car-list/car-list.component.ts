import { Component, OnInit } from '@angular/core';
import { CarService } from '../car.service';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrls: ['./car-list.component.css']
})
export class CarListComponent implements OnInit {
  cars: any[] = [];;

  constructor(private carService: CarService) {}

  ngOnInit() {
    this.carService.getCarsList().subscribe(response => {
      this.cars = response as any[];
    }, error => {
      console.error('Error fetching car list:', error);
    });
  }
}
