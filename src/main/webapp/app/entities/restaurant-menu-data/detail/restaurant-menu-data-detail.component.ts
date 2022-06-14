import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRestaurantMenuData } from '../restaurant-menu-data.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-restaurant-menu-data-detail',
  templateUrl: './restaurant-menu-data-detail.component.html',
})
export class RestaurantMenuDataDetailComponent implements OnInit {
  restaurantMenuData: IRestaurantMenuData | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantMenuData }) => {
      this.restaurantMenuData = restaurantMenuData;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
