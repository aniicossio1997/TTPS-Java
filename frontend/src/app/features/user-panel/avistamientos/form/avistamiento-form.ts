import { Component, OnInit, Input, Output, EventEmitter, inject } from '@angular/core';
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
import { FileUploadModule } from 'primeng/fileupload';
import { DatePickerModule } from 'primeng/datepicker';
import { Avistamiento, AvistamientoCreate } from '../../../../interfaces/avistamiento.interface';
import { FormErrorComponent } from '../../../../components/form-error/form-error.component';
import { AvistamientoService } from '../../../../services/avistamiento.service';
import { ToastrService } from 'ngx-toastr';

type Form = {
  descripcion: FormControl<string>;
  ubicacion: FormGroup<{
    latitud: FormControl<number>;
    longitud: FormControl<number>;
  }>;
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
  ],
})
export class AvistamientoForm implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private service = inject(AvistamientoService);
  private toastService = inject(ToastrService);

  @Input() display: boolean = false;
  @Input() publicacionId: number = -1;

  @Output() displayChange = new EventEmitter<boolean>();
  @Output() success = new EventEmitter<Avistamiento>();
  @Output() fileUpload = new EventEmitter<{ files: File[]; avistamientoId: number | null }>();

  avistamientoForm!: FormGroup<Form>;

  constructor() {}

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.avistamientoForm = this.fb.group({
      descripcion: ['', Validators.required],
      ubicacion: this.fb.group({
        latitud: this.fb.control(-1, [Validators.required]),
        longitud: this.fb.control(-1, [Validators.required]),
      }),
    });
  }

  onHide(): void {
    this.displayChange.emit(false);
    this.avistamientoForm.reset();
  }

  onSubmit(): void {
    if (this.avistamientoForm.valid) {
      const formValue = this.avistamientoForm.getRawValue();
      const avistamientoData: AvistamientoCreate = {
        descripcion: formValue.descripcion,
        ubicacion: {
          ...formValue.ubicacion,
          provincia: '',
          ciudad: '',
          barrio: '',
          municipio: '',
          departamento: '',
          idExterno: '',
          idExternoMunicipio: '',
          idExternoProvincia: '',
          idExternoDepartamento: ''
        },
        publicacionId: this.publicacionId,
      };

      this.service.create(avistamientoData).subscribe({
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

  onFileSelect(event: any): void {}
}
