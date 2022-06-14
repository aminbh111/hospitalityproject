import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurantMenuData } from '../restaurant-menu-data.model';
import { RestaurantMenuDataService } from '../service/restaurant-menu-data.service';
import { RestaurantMenuDataDeleteDialogComponent } from '../delete/restaurant-menu-data-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-restaurant-menu-data',
  templateUrl: './restaurant-menu-data.component.html',
})
export class RestaurantMenuDataComponent implements OnInit {
  restaurantMenuData?: IRestaurantMenuData[];
  isLoading = false;

  constructor(
    protected restaurantMenuDataService: RestaurantMenuDataService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.restaurantMenuDataService.query().subscribe({
      next: (res: HttpResponse<IRestaurantMenuData[]>) => {
        this.isLoading = false;
        this.restaurantMenuData = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRestaurantMenuData): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(restaurantMenuData: IRestaurantMenuData): void {
    const modalRef = this.modalService.open(RestaurantMenuDataDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.restaurantMenuData = restaurantMenuData;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
