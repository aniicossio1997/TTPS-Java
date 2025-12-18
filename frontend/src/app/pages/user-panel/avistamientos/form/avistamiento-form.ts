import { Component, OnInit, Input, Output, EventEmitter, inject, signal, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormGroup,
  Validators,
  ReactiveFormsModule,
  NonNullableFormBuilder,
  FormControl,
} from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { FileRemoveEvent, FileSelectEvent, FileUploadModule } from 'primeng/fileupload';
import { DatePickerModule } from 'primeng/datepicker';
import { Avistamiento, AvistamientoCreate } from '../../../../interfaces/avistamiento.interface';
import { FormErrorComponent } from '../../../../components/form-error/form-error.component';
import { AvistamientoService } from '../../../../services/avistamiento.service';
import { ToastrService } from 'ngx-toastr';
import { LocationPickerComponent } from '../../../../components/LocationPicker/location-picker.component';
import { UbicacionCreate } from '../../../../interfaces/ubicacion.interface';

type UbicacionControls = {
  lat: FormControl<string>;
  lng: FormControl<string>;
};

type Form = {
  descripcion: FormControl<string>;
  ubicacion: FormGroup<UbicacionControls>;
};

@Component({
  selector: 'app-avistamiento-form',
  templateUrl: './avistamiento-form.html',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DialogModule,
    InputTextModule,
    ButtonModule,
    FileUploadModule,
    DatePickerModule,
    FormErrorComponent,
    LocationPickerComponent,
  ],
})
export class AvistamientoForm implements OnInit , OnDestroy{
  private fb = inject(NonNullableFormBuilder);
  private service = inject(AvistamientoService);
  private toastService = inject(ToastrService);

  @Input() display: boolean = false;
  @Input() publicacionId: number = -1;

  @Output() displayChange = new EventEmitter<boolean>();
  @Output() success = new EventEmitter<Avistamiento>();
  @Output() fileUpload = new EventEmitter<{ files: File[]; avistamientoId: number | null }>();

  public selectedFiles = signal<File[]>([]);

  avistamientoForm!: FormGroup<Form>;

  constructor() {}
  ngOnDestroy(): void {
  }

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.avistamientoForm = this.fb.group({
      descripcion: ['', Validators.required],
      ubicacion: this.fb.group({
        lat: ['', Validators.required],
        lng: ['', Validators.required],
      }),
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.avistamientoForm.get(controlName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  onHide(): void {

    this.displayChange.emit(false);
    this.avistamientoForm.reset();
  }

  onSubmit(): void {
    if (this.avistamientoForm.valid) {
      const formValue = this.avistamientoForm.getRawValue();
      const ubicacion = this.avistamientoForm.get('ubicacion')?.value;
      if (!ubicacion) {
        return;
      }

      const avistamientoData: AvistamientoCreate = {
        descripcion: formValue.descripcion,
        ubicacion: {
          latitud: Number(formValue.ubicacion.lat),
          longitud: Number(formValue.ubicacion.lng),
        },
        publicacionId: this.publicacionId,
      };

      this.service.create(avistamientoData, this.selectedFiles()).subscribe({
        next: () => {
          this.success.emit();
          this.toastService.success('Se ha registrado el avistamiento.');
        },
      });

      this.onHide();
    } else {
      this.avistamientoForm.markAllAsTouched();
    }
  }

    onLocationSelected(ubicacionExterna: UbicacionCreate) {
      this.avistamientoForm.controls['ubicacion'].setValue({
        lat: ubicacionExterna.latitud.toString(),
        lng: ubicacionExterna.longitud.toString(),
      });
    }

  onFilesSelected(event: FileSelectEvent): void {
    const currentFiles = this.selectedFiles();
    const newFiles = [...currentFiles, ...event.files];
    this.selectedFiles.set(newFiles);

  }

  onFileRemoved(event: FileRemoveEvent): void {
    const removedFile = event.file;
    const currentFiles = this.selectedFiles();

    const updatedFiles = currentFiles.filter((f) => f !== removedFile);

    this.selectedFiles.set(updatedFiles);
  }
}
