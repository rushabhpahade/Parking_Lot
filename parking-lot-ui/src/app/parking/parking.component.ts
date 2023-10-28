import { Component } from '@angular/core';
import { CarService } from '../car.service';

@Component({
  selector: 'app-parking',
  templateUrl: './parking.component.html',
  styleUrls: ['./parking.component.css']
})
export class ParkingComponent {
  registrationNumber: string ='';
  color: string = '';

  constructor(private carService: CarService) {}

  parkCar() {
    this.carService.parkCar(this.registrationNumber, this.color).subscribe(response => {
      console.log('Car parked successfully:', response);
    }, error => {
      console.error('Error parking car:', error);
    });
  }
}
