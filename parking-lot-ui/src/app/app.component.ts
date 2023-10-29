// app.component.ts
import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = "Parking Lot Application"
  baseUrl = 'http://localhost:8080/parkinglot'; // Replace with your backend URL
  searchColor: string ='';
  registrationNumber: string='';
  resultMessage: string='';
  carResults: any[] = [];
  registrationNumbers: string[] = [];
  ticketNumber: number = 0;
  ticketNumbers: number[] = [];
  carDetails: any;
  ticketResults: any[]= [];

  constructor(private http: HttpClient) { }

  createParkingLot() {
    this.http.post(this.baseUrl + '/createParkingLot', {}).subscribe(
      (response: any) => {
        this.resultMessage = response;
      },
      (error: any) => {
        this.resultMessage = 'Error creating parking lot';
      }
    );
  }

  parkCar() {
    const params = {
      registrationNumber: this.registrationNumber,
      color: this.searchColor
    };

    this.http.post(this.baseUrl + '/parkCar', {}, { params }).subscribe(
      (response: any) => {
        this.resultMessage = response;
      },
      (error: any) => {
        this.resultMessage = 'Error parking car';
      }
    );
  }

  getCarsByColor() {
    this.http.get(this.baseUrl + '/cars/' + this.searchColor).subscribe(
      (response: any) => {
        this.carResults = response;
      },
      (error: any) => {
        this.carResults = [];
      }
    );
  }

  getRegistrationNumbersByColor() {
    this.http.get(this.baseUrl + '/registrationNumbersByColor', { params: { color: this.searchColor } }).subscribe(
      (response: any) => {
        this.registrationNumbers = response;
      },
      (error: any) => {
        this.registrationNumbers = [];
      }
    );
  }

  getTicketNumberByRegistrationNumber() {
    this.http.get(this.baseUrl + '/ticketNumberByRegistrationNumber', { params: { registrationNumber: this.registrationNumber } }).subscribe(
      (response: any) => {
        this.ticketNumber = response;
      },
      (error: any) => {
        this.ticketNumber = 0;
      }
    );
  }

  getTicketNumbersByColor() {
    this.http.get(this.baseUrl + '/ticketNumbersByColor', { params: { color: this.searchColor } }).subscribe(
      (response: any) => {
        this.ticketNumbers = response;
      },
      (error: any) => {
        this.ticketNumbers = [];
      }
    );
  }

  getCarByRegistrationNumber() {
    this.http.get(this.baseUrl + '/ticket/' + this.registrationNumber).subscribe(
      (response: any) => {
        this.carDetails = response;
      },
      (error: any) => {
        this.carDetails = null;
      }
    );
  }

  getTicketsByColor() {
    this.http.get(this.baseUrl + '/tickets/' + this.searchColor).subscribe(
      (response: any) => {
        this.ticketResults = response;
      },
      (error: any) => {
        this.ticketResults = [];
      }
    );
  }
}
