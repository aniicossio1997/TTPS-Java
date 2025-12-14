import {
  Especie,
  Publicacion,
  Tamanio,
} from './../../../../../interfaces/publicacion.interface';
import { Component, OnInit, Input, Output, EventEmitter, signal, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  Validators,
  FormsModule,
  ReactiveFormsModule,
  NonNullableFormBuilder,
} from '@angular/forms';
import { SelectItem } from 'primeng/api';
import {
  PublicacionCreate,
} from '../../../../../interfaces/publicacion.interface';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MultiSelectModule } from 'primeng/multiselect';
import { FieldsetModule } from 'primeng/fieldset';
import { FileUploadModule } from 'primeng/fileupload';
import { RadioButtonModule } from 'primeng/radiobutton';
import { DividerModule } from 'primeng/divider';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectButtonModule } from 'primeng/selectbutton';
import { SelectModule } from 'primeng/select';
import { MessageModule } from 'primeng/message';
import { UbicacionCreate } from '../../../../../interfaces/ubicacion.interface';
import { FormErrorComponent } from '../../../../../components/form-error/form-error.component';
import { RouterModule } from '@angular/router';
import { LocationPickerComponent, UbicacionSeleccionada } from '../../../../../components/LocationPicker/location-picker.component';
import { UbicacionExternaResponse } from '../../../../../interfaces/ubicacionExternaResponse';

enum TipoPublicacion {
  PROPIO = 'Propio',
  AJENO = 'Ajeno',
}

@Component({
  selector: 'app-publicacion-form',
  templateUrl: './publicacion-form.html',
  styleUrl: './publicacion-form.scss',
  imports: [
    InputTextModule,
    RadioButtonModule,
    PasswordModule,
    ButtonModule,
    CardModule,
    PasswordModule,
    FormsModule,
    DividerModule,
    MessageModule,
    FieldsetModule,
    FileUploadModule,
    ReactiveFormsModule,
    MultiSelectModule,
    CommonModule,
    DatePickerModule,
    SelectButtonModule,
    SelectModule,
    FormErrorComponent,
    RouterModule,
    LocationPickerComponent,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PublicacionFormComponent implements OnInit {
  @Input() publicacionId: number | null = null;

  @Input() publicacionExistente: Publicacion | null = null;

  @Output() formSubmit = new EventEmitter<PublicacionCreate>();

  ubicacionExterna = signal<UbicacionExternaResponse | null>(null);

  publicacionForm;
  tipoPublicacionOpciones: SelectItem<TipoPublicacion>[];
  especies: Especie[] = ['Perro', 'Gato', 'Otro'];
  tamanos: { value: Tamanio; label: string }[] = [
    { value: 'PEQUENO', label: 'Pequeño' },
    { value: 'MEDIANO', label: 'Mediano' },
    { value: 'GRANDE', label: 'Grande' },
  ];

  submitLabel: string = 'Crear Publicación';

  constructor(private fb: NonNullableFormBuilder) {
    this.tipoPublicacionOpciones = [
      { label: 'Mascota Propia', value: TipoPublicacion.PROPIO },
      { label: 'Mascota Ajena (Encontrada/Vista)', value: TipoPublicacion.AJENO },
    ];

    this.publicacionForm = this.fb.group({
      tipo: this.fb.control<TipoPublicacion>(TipoPublicacion.PROPIO, [Validators.required]),
      especie: this.fb.control<string>('', Validators.required),
      nombre: this.fb.control<string>('', Validators.required),
      descripcion: this.fb.control<string>('', Validators.maxLength(256)),
      fechaPerdida: this.fb.control<Date>(new Date(), Validators.required),
      tamanio: this.fb.control<string>('PEQUENO'),
      color: this.fb.control<string>('Negro'),
      ubicacion: this.fb.control<UbicacionCreate | undefined>(undefined, Validators.required),
    });
  }

  ngOnInit(): void {
    if (this.publicacionExistente) {
      this.loadExistingData(this.publicacionExistente);
      this.submitLabel = 'Actualizar Publicación';
    } else if (this.publicacionId) {
      this.submitLabel = 'Actualizar Publicación';
    }

    this.publicacionForm.get('tipo')?.valueChanges.subscribe((tipo) => {
      const nombreControl = this.publicacionForm.get('nombre');
      if (tipo === TipoPublicacion.AJENO) {
        nombreControl?.clearValidators();
      } else {
        nombreControl?.setValidators([Validators.required, Validators.maxLength(50)]);
      }
      nombreControl?.updateValueAndValidity();
    });
  }

  private loadExistingData(data: Publicacion): void {
    const tipo = ['PERDIDO_AJENO', 'PERDIDO_PROPIO'].includes(data.estado?.estado ?? '')
      ? TipoPublicacion.PROPIO
      : TipoPublicacion.AJENO;

    this.publicacionForm.patchValue({
      tipo: tipo,
      especie: data.especie,
      nombre: data.nombre,
      descripcion: data.descripcion,
      color: data.color,
      tamanio: data.tamanio,
      ubicacion: data.ubicacion,
    });

    this.publicacionForm.get('tipo')?.updateValueAndValidity();
  }

  COLOR_MAP: Record<string, string> = {
    negro: '#000000',
    blanco: '#FFFFFF',
    marron: '#8B4513',
    gris: '#808080',
    otro: '#D3D3D3',
  };

  getColors() {
    return Object.entries(this.COLOR_MAP);
  }

  onSubmit(): void {
    if (this.publicacionForm.valid) {
      const formValue = this.publicacionForm.value;

      const { nombre, descripcion, tipo, color, especie, tamanio, ubicacion } = formValue;
      if (!color || !especie || !tamanio || !ubicacion || !nombre || !descripcion || !ubicacion)
        return;

      const estadoActual = this.publicacionExistente?.estado?.estado;
      const publicacionData: PublicacionCreate = {
        nombre: nombre || 'Desconocido',
        descripcion: descripcion,
        color: color,
        especie: especie,
        tamanio: tamanio,
        ubicacion: {
          latitud: ubicacion.longitud,
          longitud: ubicacion.longitud,
          provincia: '',
          idExternoProvincia: '',
          municipio: '',
          idExternoMunicipio: '',
          departamento: '',
          idExternoDepartamento: ''
        },
        estado:
          estadoActual || (tipo === TipoPublicacion.PROPIO ? 'PERDIDO_PROPIO' : 'PERDIDO_AJENO'),
      };
      this.formSubmit.emit(publicacionData);

      console.log(`${this.publicacionId ? 'Actualizando' : 'Creando'} Publicación...`);
    } else {
      console.error('El formulario no es válido.');
      this.publicacionForm.markAllAsTouched();
    }
  }

  isInvalid(controlName: string): boolean {
    const control = this.publicacionForm.get(controlName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  hasError(controlName: string, error: string): boolean {
    return !!this.publicacionForm.get(controlName)?.hasError(error);
  }

  onUpload(event: any) {
    console.log('Archivos recibidos para el ID:', this.publicacionId || 'nuevo');
  }

  onLocationSelected(ubicacion: UbicacionSeleccionada) {

    null;
  }

  hasUbicacionExterna(): boolean {
    const ubicacion = this.publicacionForm.get('ubicacion')?.value;

    return !!(ubicacion && ubicacion.latitud && ubicacion.longitud);
  }

}
