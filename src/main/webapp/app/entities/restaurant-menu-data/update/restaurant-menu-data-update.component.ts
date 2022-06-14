import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRestaurantMenuData, RestaurantMenuData } from '../restaurant-menu-data.model';
import { RestaurantMenuDataService } from '../service/restaurant-menu-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IRestaurantMenu } from 'app/entities/restaurant-menu/restaurant-menu.model';
import { RestaurantMenuService } from 'app/entities/restaurant-menu/service/restaurant-menu.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-restaurant-menu-data-update',
  templateUrl: './restaurant-menu-data-update.component.html',
})
export class RestaurantMenuDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  restaurantMenusSharedCollection: IRestaurantMenu[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    restaurantMenu: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected restaurantMenuDataService: RestaurantMenuDataService,
    protected restaurantMenuService: RestaurantMenuService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurantMenuData }) => {
      this.updateForm(restaurantMenuData);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('hospitalityprojectApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurantMenuData = this.createFromForm();
    if (restaurantMenuData.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantMenuDataService.update(restaurantMenuData));
    } else {
      this.subscribeToSaveResponse(this.restaurantMenuDataService.create(restaurantMenuData));
    }
  }

  trackRestaurantMenuById(_index: number, item: IRestaurantMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurantMenuData>>): void {
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

  protected updateForm(restaurantMenuData: IRestaurantMenuData): void {
    this.editForm.patchValue({
      id: restaurantMenuData.id,
      lang: restaurantMenuData.lang,
      title: restaurantMenuData.title,
      content: restaurantMenuData.content,
      image: restaurantMenuData.image,
      imageContentType: restaurantMenuData.imageContentType,
      restaurantMenu: restaurantMenuData.restaurantMenu,
    });

    this.restaurantMenusSharedCollection = this.restaurantMenuService.addRestaurantMenuToCollectionIfMissing(
      this.restaurantMenusSharedCollection,
      restaurantMenuData.restaurantMenu
    );
  }

  protected loadRelationshipsOptions(): void {
    this.restaurantMenuService
      .query()
      .pipe(map((res: HttpResponse<IRestaurantMenu[]>) => res.body ?? []))
      .pipe(
        map((restaurantMenus: IRestaurantMenu[]) =>
          this.restaurantMenuService.addRestaurantMenuToCollectionIfMissing(restaurantMenus, this.editForm.get('restaurantMenu')!.value)
        )
      )
      .subscribe((restaurantMenus: IRestaurantMenu[]) => (this.restaurantMenusSharedCollection = restaurantMenus));
  }

  protected createFromForm(): IRestaurantMenuData {
    return {
      ...new RestaurantMenuData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      restaurantMenu: this.editForm.get(['restaurantMenu'])!.value,
    };
  }
}
