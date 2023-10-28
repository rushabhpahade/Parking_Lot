import { Component } from '@angular/core';
import { CarService } from '../car.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  searchColor: string ='';

  constructor(private carService: CarService) {}

  searchCarsByColor() {
    this.carService.searchCarsByColor(this.searchColor).subscribe(response => {
      console.log('Cars of color ' + this.searchColor + ':', response);
    }, error => {
      console.error('Error searching for cars:', error);
    });
  }
}
