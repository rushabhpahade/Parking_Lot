import { Component } from '@angular/core';
import { CarService } from '../car.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  searchColor: string = '';
  searchResults: any[] = []; // Store search results here

  constructor(private carService: CarService) {}

  searchCarsByColor() {
    this.carService.searchCarsByColor(this.searchColor).subscribe(
      (response: any) => {
        this.searchResults = response; // Store the search results
      },
      (error) => {
        console.error('Error searching for cars:', error);
      }
    );
  }
}
