// car.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CarService {
  private baseUrl = 'http://localhost:8080/parkinglot'; 

  constructor(private http: HttpClient) {}

  parkCar(registrationNumber: string, color: string) {
    return this.http.post(this.baseUrl + '/parkCar', null, {
      params: {
        registrationNumber,
        color
      }
    });
  }

  searchCarsByColor(searchColor: string) {
    return this.http.get(this.baseUrl + '/cars/' + searchColor);
  }

  getCarsList() {
    return this.http.get(this.baseUrl + '/cars');
  }
}
