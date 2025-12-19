import { Especie, Publicacion, Tamanio } from './../../../../../interfaces/publicacion.interface';
import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  signal,
  ChangeDetectionStrategy,
  inject,
  input,
  computed,

} from '@angular/core';
import { CommonModule, LocationStrategy , Location} from '@angular/common';
import {
  Validators,
  FormsModule,
  ReactiveFormsModule,
  NonNullableFormBuilder,
} from '@angular/forms';
import { SelectItem } from 'primeng/api';
import { PublicacionCreate } from '../../../../../interfaces/publicacion.interface';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MultiSelectModule } from 'primeng/multiselect';
import { FieldsetModule } from 'primeng/fieldset';
import {
  FileRemoveEvent,
  FileSelectEvent,
  FileUploadModule,
} from 'primeng/fileupload';
import { RadioButtonModule } from 'primeng/radiobutton';
import { DividerModule } from 'primeng/divider';
import { DatePickerModule } from 'primeng/datepicker';
import { SelectButtonModule } from 'primeng/selectbutton';
import { SelectModule } from 'primeng/select';
import { MessageModule } from 'primeng/message';
import { FormErrorComponent } from '../../../../../components/form-error/form-error.component';
import { RouterModule } from '@angular/router';
import {
  LocationPickerComponent,
} from '../../../../../components/LocationPicker/location-picker.component';
import { UbicacionExternaResponse } from '../../../../../interfaces/ubicacionExternaResponse';
import { FotoLinkDTO } from '../../../../../interfaces/fotoLinkDTO';
import { Ubicacion, UbicacionCreate } from '../../../../../interfaces/ubicacion.interface';
import { ApiStatus } from '../../../../../interfaces/local/EnumApiStatus.enum';

enum TipoPublicacion {
  PROPIO = 'Propio',
  AJENO = 'Ajeno',
}
type PublicacionSubmitEvent = [PublicacionCreate, File[]];
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
  location= inject(LocationStrategy)

  statusForm = input.required<ApiStatus>()
  readonly isLoading = computed(() => this.statusForm() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this.statusForm() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this.statusForm() === ApiStatus.ERROR);


  @Input() publicacionId: number | null = null;

  @Input() publicacionExistente: Publicacion | null = null;

  @Output() formSubmit = new EventEmitter<PublicacionSubmitEvent>();

  ubicacionExterna = signal<UbicacionExternaResponse | null>(null);
  public selectedFiles = signal<File[]>([]);
  public fotosExistentes = signal<FotoLinkDTO[]>([]);

  ubicacionPrecargada = signal<UbicacionCreate| null>(null);


  publicacionForm;
  tipoPublicacionOpciones: SelectItem<TipoPublicacion>[];
  especies: Especie[] = ['Perro', 'Gato', 'Ave', 'Otro'];
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
      fechaPerdida: this.fb.control<Date | null>(null, Validators.required),
      tamanio: this.fb.control<string>('PEQUENO'),
      color: this.fb.control<string>('Negro'),
      ubicacion: this.fb.group({
        lat: ['', Validators.required],
        lng: ['', Validators.required],
        provincia: [''],
        idExternoProvincia: [''],
        municipio: [''],
        idExternoMunicipio: [''],
        departamento: [''],
        idExternoDepartamento: [''],
      }),
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
      fechaPerdida: data.fecha ? new Date(data.fecha) : null,
      ubicacion:{
      lat: String(data.ubicacion!.latitud!),
      lng: String(data.ubicacion.longitud),

      },

    });



    this.ubicacionPrecargada.set({...data.ubicacion!})


    this.publicacionForm.get('tipo')?.updateValueAndValidity();
    if (data.fotos?.length) {
      this.fotosExistentes.set(data.fotos);
    }


  }

  COLOR_MAP: Record<string, string> = {
    negro: '#000000',
    blanco: '#FFFFFF',
    marron: '#8B4513',
    gris: '#808080',
    amarillo: '#fded0aff',
  };

  getColors() {
    return Object.entries(this.COLOR_MAP);
  }

  async onSubmit() {

    if (this.publicacionForm.valid) {
      const formValue = this.publicacionForm.value;

      const { nombre, descripcion, tipo, color, especie, tamanio, ubicacion, fechaPerdida } = formValue;

      if (!color || !especie || !tamanio || !ubicacion || !nombre || !ubicacion)
        return;

      const estadoActual = this.publicacionExistente?.estado?.estado;
      const publicacionData: PublicacionCreate = {
        nombre: nombre || 'Desconocido',
        descripcion: descripcion,
        color: color,
        especie: especie,
        tamanio: tamanio,
        fecha: fechaPerdida!,

        ubicacion: {
          latitud: Number(ubicacion!.lat!),
          longitud: Number(ubicacion.lng),
        },
        estado:
          estadoActual || (tipo === TipoPublicacion.PROPIO ? 'PERDIDO_PROPIO' : 'PERDIDO_AJENO'),
      };


      this.formSubmit.emit([publicacionData, this.selectedFiles()]);


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

  onFilesSelected(event: FileSelectEvent): void {
    this.fotosExistentes.set([]);
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

  onFilesClear(): void {
    this.selectedFiles.set([]);
  }

  onLocationSelected(ubicacionExterna: UbicacionCreate) {
    const ubicacionPrevia : UbicacionCreate ={...ubicacionExterna} as UbicacionCreate;
    this.ubicacionPrecargada.set({ ...ubicacionPrevia });

    this.publicacionForm.controls['ubicacion'].setValue({
      lat: ubicacionExterna.latitud.toString(),
      lng: ubicacionExterna.longitud.toString(),
      provincia: ubicacionExterna?.provincia ?? '',
      idExternoProvincia: ubicacionExterna?.idExternoProvincia ?? '',
      municipio: ubicacionExterna?.municipio ?? '',
      idExternoMunicipio: ubicacionExterna?.idExternoMunicipio ?? '',
      departamento: ubicacionExterna?.departamento ?? '',
      idExternoDepartamento: ubicacionExterna?.idExternoDepartamento ?? '',
    });
  }

  hasUbicacionExterna(): boolean {
    const ubicacion = this.publicacionForm.get('ubicacion')?.value;

    return !!ubicacion && ubicacion.lat !== '' && ubicacion.lng !== '';
  }
  onBack(){
      this.location.back();

  }
}
