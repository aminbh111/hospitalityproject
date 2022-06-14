import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRestaurantMenu, RestaurantMenu } from '../restaurant-menu.model';
import { RestaurantMenuService } from '../service/restaurant-menu.service';
import { Position } from 'app/entities/enumerations/position.model';

@Component({
  selector: 'jhi-restaurant-menu-update',
  templateUrl: './restaurant-menu-update.component.html',
})
export class RestaurantMenuUpdateComponent implements OnInit {
  isSaving = false;
  positionValues = Object.keys(Position);

  editForm = this.fb.group({
    id: [],
    date: [],
    publish: [],
    contentPosition: [],
    imagePosition: [],
  });

  constructor(
    protected restaurantMenuService: RestaurantMenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantMenu }) => {
      if (restaurantMenu.id === undefined) {
        const today = dayjs().startOf('day');
        restaurantMenu.date = today;
      }

      this.updateForm(restaurantMenu);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurantMenu = this.createFromForm();
    if (restaurantMenu.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantMenuService.update(restaurantMenu));
    } else {
      this.subscribeToSaveResponse(this.restaurantMenuService.create(restaurantMenu));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurantMenu>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(restaurantMenu: IRestaurantMenu): void {
    this.editForm.patchValue({
      id: restaurantMenu.id,
      date: restaurantMenu.date ? restaurantMenu.date.format(DATE_TIME_FORMAT) : null,
      publish: restaurantMenu.publish,
      contentPosition: restaurantMenu.contentPosition,
      imagePosition: restaurantMenu.imagePosition,
    });
  }

  protected createFromForm(): IRestaurantMenu {
    return {
      ...new RestaurantMenu(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      publish: this.editForm.get(['publish'])!.value,
      contentPosition: this.editForm.get(['contentPosition'])!.value,
      imagePosition: this.editForm.get(['imagePosition'])!.value,
    };
  }
}
