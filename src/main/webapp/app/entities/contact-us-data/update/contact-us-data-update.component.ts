import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactUsData, ContactUsData } from '../contact-us-data.model';
import { ContactUsDataService } from '../service/contact-us-data.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IContactUs } from 'app/entities/contact-us/contact-us.model';
import { ContactUsService } from 'app/entities/contact-us/service/contact-us.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-contact-us-data-update',
  templateUrl: './contact-us-data-update.component.html',
})
export class ContactUsDataUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  contactusesSharedCollection: IContactUs[] = [];

  editForm = this.fb.group({
    id: [],
    lang: [],
    title: [],
    content: [],
    image: [],
    imageContentType: [],
    contactUs: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected contactUsDataService: ContactUsDataService,
    protected contactUsService: ContactUsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactUsData }) => {
      this.updateForm(contactUsData);

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
    const contactUsData = this.createFromForm();
    if (contactUsData.id !== undefined) {
      this.subscribeToSaveResponse(this.contactUsDataService.update(contactUsData));
    } else {
      this.subscribeToSaveResponse(this.contactUsDataService.create(contactUsData));
    }
  }

  trackContactUsById(_index: number, item: IContactUs): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactUsData>>): void {
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

  protected updateForm(contactUsData: IContactUsData): void {
    this.editForm.patchValue({
      id: contactUsData.id,
      lang: contactUsData.lang,
      title: contactUsData.title,
      content: contactUsData.content,
      image: contactUsData.image,
      imageContentType: contactUsData.imageContentType,
      contactUs: contactUsData.contactUs,
    });

    this.contactusesSharedCollection = this.contactUsService.addContactUsToCollectionIfMissing(
      this.contactusesSharedCollection,
      contactUsData.contactUs
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactUsService
      .query()
      .pipe(map((res: HttpResponse<IContactUs[]>) => res.body ?? []))
      .pipe(
        map((contactuses: IContactUs[]) =>
          this.contactUsService.addContactUsToCollectionIfMissing(contactuses, this.editForm.get('contactUs')!.value)
        )
      )
      .subscribe((contactuses: IContactUs[]) => (this.contactusesSharedCollection = contactuses));
  }

  protected createFromForm(): IContactUsData {
    return {
      ...new ContactUsData(),
      id: this.editForm.get(['id'])!.value,
      lang: this.editForm.get(['lang'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      contactUs: this.editForm.get(['contactUs'])!.value,
    };
  }
}
