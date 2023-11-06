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
  message: string = "";
  constructor(private carService: CarService) {}

  parkCar() {
    this.carService.parkCar(this.registrationNumber, this.color).subscribe(response => {
      this.message = response as string;
      console.log('Car parked successfully:', this.message);
    }, error => {
      console.error('Error parking car:', error);
    });
  }
}
