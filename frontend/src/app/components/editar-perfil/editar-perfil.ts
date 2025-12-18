import {
  LocationPickerComponent,
} from './../LocationPicker/location-picker.component';
import {
  Component,
  computed, inject,
  input,
  OnInit,
  output,
  signal,
  untracked
} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { Message } from 'primeng/message';
import { Tooltip } from 'primeng/tooltip';
import { InputTextModule } from 'primeng/inputtext';
import { InputMaskModule } from 'primeng/inputmask';
import { PerfilByUserStoreService } from '../../store/perfilByUser.stored.service';
import { UsuarioService } from '../../services/usuario.service';
import { CommonModule } from '@angular/common';
import { ApiStatus } from '../../interfaces/local/EnumApiStatus.enum';
import { ToastrService } from 'ngx-toastr';
import { UsuarioUpdateRequest } from '../../interfaces/usuarioUpdateRequest.interface';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthStoreService } from '../../store/auth.stored.service';
import { UbicacionCreate } from '../../interfaces/ubicacion.interface';


type UbicacionControls = {
  lat: FormControl<string>;
  lng: FormControl<string>;

};

type FormEdit = {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  phone: FormControl<string>;
  ubicacion: FormGroup<UbicacionControls>;

  foto: FormControl<File | null>;
};

@Component({
  selector: 'app-editar-perfil',
  imports: [
    ButtonModule,
    ReactiveFormsModule,
    Message,
    LocationPickerComponent,
    Tooltip,
    InputTextModule,
    InputMaskModule,
    CommonModule,
  ],
  templateUrl: './editar-perfil.html',
  styleUrl: './editar-perfil.scss',
  providers: [UsuarioService, PerfilByUserStoreService],
})
export class EditarPerfil implements OnInit {
  private readonly toastr = inject(ToastrService);
  private readonly authStore= inject(AuthStoreService)

  private readonly fb = inject(FormBuilder).nonNullable;
  readonly perfilStore = inject(PerfilByUserStoreService);

  private readonly _serviceUsuario = inject(UsuarioService);

  onCloseEditarPerfil = output<void>();
  onSuccessEdit = output<void>();
  idUsuario = input.required<number>();

  // Foto actual del usuario (perfil ya guardado)
  fotoActual = 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=200';

  readonly editForm: FormGroup<FormEdit> = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
    ubicacion: this.fb.group({
      lat: ['', Validators.required],
      lng: ['', Validators.required],

    }),

    // 👉 AQUÍ agregamos el archivo dentro del form
    foto: this.fb.control<File | null>(null), // 👈 mismo nombre que en la interface
  });

  public errorMensaje = signal<string>('')
  private _status = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError = computed(() => this._status() === ApiStatus.ERROR);

  ubicacionPrecargada = signal<UbicacionCreate | null>(null);

  // Esto es lo que se muestra como preview
  previewFoto = signal<string | null>(null);
  existingFotoUrl = signal<string | null>(null);

  // Archivo real seleccionado
  fotoFile: File | null = null;

  constructor() {}

  ngOnInit(): void {
    this.perfilStore._getPerfil(this.idUsuario(), () => {
      this._actualizarFormulario();
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.editForm.controls.foto.setValue(file);
    this.previewFoto.set(URL.createObjectURL(file));
  }

  onEliminarFoto() {
    this.previewFoto.set(null);
    this.editForm.get('foto')?.setValue(null);
  }

  isInvalidTOBTN(controlName: string) {
    const control = this.editForm.get(controlName);

    if (controlName === 'ubicacion') {
      return !!control && control.invalid;
    }

    return control?.invalid;
  }

  isInvalid(controlName: string) {
    const control = this.editForm.get(controlName);

    if (controlName === 'ubicacion') {
      return !!control && control.invalid && (control.touched || control.dirty);
    }

    return control?.invalid && (control.touched || control.dirty);
  }
  hasUbicacionExterna(): boolean {
    const ubicacion = this.editForm.get('ubicacion')?.value;

    return !!ubicacion && ubicacion.lat !== '' && ubicacion.lng !== '';
  }

  onLocationSelected(ubicacionExterna: UbicacionCreate) {

    this.editForm.controls['ubicacion'].setValue({
      lat: ubicacionExterna.latitud.toString(),
      lng: ubicacionExterna.longitud.toString(),

    });
    this.ubicacionPrecargada.set({ ...ubicacionExterna });
    // this.visibleMapa = false;
    // acá podés guardar en el formulario, mandar al backend, etc.
  }

  async onSubmit() {
    if (this.editForm.invalid) {
      return;
    }

    await this._callApiPutUser(this.editForm);
  }

  onCloseEditarPerfilLocal() {
    this.editForm.reset();
    this.onCloseEditarPerfil.emit();
  }

  private async urlToFile(url: string, fileName = 'foto-perfil.jpg'): Promise<File> {
    const res = await fetch(url); // si necesita token, te paso alternativa abajo
    if (!res.ok) throw new Error(`No se pudo descargar la imagen: ${res.status}`);

    const blob = await res.blob();
    return new File([blob], fileName, { type: blob.type || 'image/jpeg' });
  }

  private _actualizarFormulario() {
    const perfil = this.perfilStore.perfilDeUsuario();

    // Si no hay perfil, o si el formulario YA fue "tocado" (dirty), no sobrescribir.
    // Ojo: 'dirty' se pone true apenas escribes una letra.
    if (!perfil) return;

    // Usamos untracked para leer valores del form sin crear dependencia circular
    const isFormDirty = untracked(() => this.editForm.dirty);

    // 1) Patch datos “simples”
    this.editForm.patchValue({
      firstName: perfil.nombre ?? '',
      lastName: perfil.apellido ?? '',
      email: perfil.email ?? '',
      phone: perfil.telefono ?? '',
    });

    // 2) Patch ubicación (si existe)
    if (perfil.ubicacion) {
      this.editForm.get('ubicacion')?.patchValue({
        lat: String(perfil.ubicacion.latitud ?? ''),
        lng: String(perfil.ubicacion.longitud ?? ''),

      });

        this.ubicacionPrecargada.set({
          latitud: perfil.ubicacion.latitud ?? '',
          longitud: perfil.ubicacion.longitud ?? '',

          departamento: perfil.ubicacion.departamento,
          idExternoDepartamento: perfil.ubicacion.idExternoDepartamento,


          municipio: perfil.ubicacion.municipio,
          idExternoMunicipio: perfil.ubicacion.idExternoMunicipio,

          provincia: perfil.ubicacion.provincia,
          idExternoProvincia: perfil.ubicacion.idExternoProvincia,
        });
      }

    // 3) Foto (si viene link/url desde backend)
    // Ej: perfil.foto?.url (según tu DTO)
    const fotoUrl = perfil.fotoLink?.url ?? null;

    this.existingFotoUrl.set(fotoUrl);
    this.previewFoto.set(fotoUrl);

    // 4) Importante: NO intentes setear el File real acá (por seguridad del browser)
    // El control 'foto' queda null hasta que el usuario elija un archivo.
    //this.editForm.get('foto')?.setValue(null);
  }

  private async _callApiPutUser(entityForm: FormGroup<FormEdit>) {
    // 1) Validación
    if (entityForm.invalid) {
      entityForm.markAllAsTouched();
      this.toastr.error('Revisá los campos del formulario', 'Formulario inválido');
      return;
    }

    this._status.set(ApiStatus.LOADING);

    // 2) Datos del formulario
    const formValue = entityForm.getRawValue();
    const ubicacionForm = formValue.ubicacion;

    const dataToPut: UsuarioUpdateRequest = {
      apellido: formValue.lastName ?? '',
      email: formValue.email ?? '',
      nombre: formValue.firstName ?? '',
      telefono: formValue.phone ?? '',
      ubicacion: {
        latitud: Number(ubicacionForm.lat),
        longitud: Number(ubicacionForm.lng),
      },
    };

    // 3) Foto a enviar
    let fileToSend: File | undefined = formValue.foto ?? undefined;

    // Caso: no eligió nueva foto y la que se muestra es la existente
    // (solo si tu backend borra foto cuando no llega file)
    if (!fileToSend && this.existingFotoUrl() && this.previewFoto() === this.existingFotoUrl()) {
      try {
        fileToSend = await this.urlToFile(this.existingFotoUrl()!, 'foto-perfil.jpg');
      } catch (error) {
        console.warn('No se pudo recuperar la foto existente para reenviarla', error);
        fileToSend = undefined;
      }
    }

    // Caso: usuario eliminó (preview null) => fileToSend queda undefined (tu backend lo interpretará como borrar)

    // 4) Llamada API (✅ usar fileToSend)

    this.errorMensaje.set('')
    this._serviceUsuario.putUpdateUsuario(this.idUsuario(), dataToPut, fileToSend).subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);
        this.toastr.success('Se guardaron los datos', 'Éxito');
        this.onCloseEditarPerfilLocal();
        this.onSuccessEdit.emit()

        if(resp.id == this.authStore.usuario()?.id){
          this.authStore.updateUsuarioEnSesion(resp);
        }

      },
      error: (err:HttpErrorResponse) => {
        const mensajeError = err?.error?.message ||' Error al guardar el perfil'
        this.errorMensaje.set(mensajeError)
        this._status.set(ApiStatus.ERROR);
        this.toastr.error(mensajeError, 'Error');
      },
    });
  }
}
