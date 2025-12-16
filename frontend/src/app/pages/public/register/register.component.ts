import { Component, inject, OnInit, signal, ViewChild, computed, ChangeDetectionStrategy } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
  FormsModule,
  ValidationErrors,
} from '@angular/forms';

// PrimeNG
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { AbstractControl } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { DividerModule } from 'primeng/divider';
import { Router, RouterLink } from '@angular/router';
import { MessageModule } from 'primeng/message';
import { LocationPickerComponent, UbicacionSeleccionada } from '../../../components/LocationPicker/location-picker.component';
import { CommonModule } from '@angular/common';
import { Dialog } from 'primeng/dialog';
import { UbicacionExternaResponse } from '../../../interfaces/ubicacionExternaResponse';
import { AuthService } from '../../../services/auth.service';
import { ApiStatus } from '../../../interfaces/local/EnumApiStatus.enum';
import { RegisterRequest } from '../../../interfaces/registerRequest.interface';
import { EnumRolUsuario } from '../../../interfaces/local/rol-usuario.enum';
import { ToastrService } from 'ngx-toastr';
import { InputMaskModule } from 'primeng/inputmask';
import { Tooltip } from 'primeng/tooltip';
import { PUBLIC_ROUTES_ENUM } from '../public.routes';

function passwordMatchValidator(form: AbstractControl) {
  const password = form.get('password')?.value;
  const confirm = form.get('confirmPassword')?.value;

  if (password !== confirm) {
    form.get('confirmPassword')?.setErrors({ mismatch: true });
  } else {
    const errors = form.get('confirmPassword')?.errors;
    if (errors) {
      delete errors['mismatch'];
      if (Object.keys(errors).length === 0) {
        form.get('confirmPassword')?.setErrors(null);
      }
    }
  }

  return null;
}

type UbicacionControls = {
  lat: FormControl<string>;
  lng: FormControl<string>;
  provincia: FormControl<string>;
  idExternoProvincia: FormControl<string>;
  municipio: FormControl<string>;
  idExternoMunicipio: FormControl<string>;
  departamento: FormControl<string>;
  idExternoDepartamento: FormControl<string>;
};
type RegisterForm = {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  phone: FormControl<string>;
  ubicacion: FormGroup<UbicacionControls>;
  password: FormControl<string>;
  confirmPassword: FormControl<string>;
};
@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,

    InputTextModule,

    PasswordModule,
    ButtonModule,
    CardModule,
    PasswordModule,
    FormsModule,
    DividerModule,
    MessageModule,

    LocationPickerComponent,
    InputMaskModule,
    Tooltip,
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  schemas: [],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RegisterComponent implements OnInit {
  private readonly router = inject(Router);
  readonly serviceAuth = inject(AuthService);
  private readonly toastr = inject(ToastrService);
  private readonly _status = signal<ApiStatus>(ApiStatus.INIT);

  ubicacionPrecargada = signal<UbicacionSeleccionada | null>(null);

  onLocationSelected( ubicacionExterna: UbicacionSeleccionada) {
      this.ubicacionPrecargada.set({...ubicacionExterna})

      this.registerForm.controls['ubicacion'].setValue({
        lat: ubicacionExterna.lat.toString(),
        lng: ubicacionExterna.lng.toString(),
        provincia: ubicacionExterna?.provincia ?? '',
        idExternoProvincia: ubicacionExterna?.idExternoProvincia ?? '',
        municipio: ubicacionExterna?.municipio ?? '',
        idExternoMunicipio: ubicacionExterna?.idExternoMunicipio ?? '',
        departamento: ubicacionExterna?.departamento ?? '',
        idExternoDepartamento: ubicacionExterna?.idExternoDepartamento ?? '',
      });


      // this.visibleMapa = false;
      // acá podés guardar en el formulario, mandar al backend, etc.
  }

  /*
   {{ubicacionExterna()!.ubicacion!.provincia!.nombre!}}, {{ubicacionExterna()!.ubicacion!.departamento!.nombre!}},
                {{ubicacionExterna()!.ubicacion!.municipio!.nombre!}}
  */


  ngOnInit(): void {}

  // 🔹 Inyectamos el FormBuilder con el nuevo patrón
  private readonly fb = inject(FormBuilder).nonNullable;

  // 🔹 Creamos el FormGroup con el método group() del FormBuilder
  //    Todos los controles son no-nullable gracias a .nonNullable
  readonly registerForm: FormGroup<RegisterForm> = this.fb.group(
    {
      firstName: this.fb.control('', [Validators.required]),
      lastName: this.fb.control('', [Validators.required]),
      email: this.fb.control('', [Validators.required, Validators.email]),
      phone: this.fb.control(''),
      ubicacion: this.fb.group({
        lat: ['', Validators.required],
        lng: ['', Validators.required],
        provincia: ['', ],
        idExternoProvincia: ['', ],
        municipio: ['', ],
        idExternoMunicipio: ['', ],
        departamento: ['',],
        idExternoDepartamento: ['',],
      }),
      password: this.fb.control('', [Validators.required, Validators.minLength(6)]),
      confirmPassword: this.fb.control('', [Validators.required]),
    },
    { validators: passwordMatchValidator }
  );

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.registerForm.markAllAsTouched();
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();

    }

    // getRawValue() para mantener bien el tipo
    const data = this.registerForm.getRawValue();

    this._onRegister();
    // acá llamás al backend
  }

  // helper (mucho más cómodo de usar en HTML)
  isInvalid(controlName: string) {
    const control = this.registerForm.get(controlName);

    if (controlName === 'ubicacion') {
      return !!control && control.invalid && (control.touched || control.dirty);
    }

    return control?.invalid && (control.touched || control.dirty);
  }

  isInvalidTOBTN(controlName: string) {
    const control = this.registerForm.get(controlName);

    if (controlName === 'ubicacion') {
      return !!control && control.invalid;
    }

    return control?.invalid;
  }

  hasUbicacionExterna(): boolean {
    const ubicacion = this.registerForm.get('ubicacion')?.value;

    return !!ubicacion && ubicacion.lat !== '' && ubicacion.lng !== '';
  }

  private _onRegister() {
    this._status.set(ApiStatus.LOADING);
    const formValue = this.registerForm.getRawValue();
    const ubicacion = this.registerForm.get('ubicacion')?.value;

    if (!ubicacion) {
      this.toastr.error('Ubicación no seleccionada.', 'Error');
      this._status.set(ApiStatus.ERROR);
      return;
    }

    const request: RegisterRequest = {
      nombre: formValue.firstName,
      apellido: formValue.lastName,
      email: formValue.email,
      password: formValue.password,
      rol: EnumRolUsuario.USUARIO_COMUN,
      telefono: formValue.phone,
      ubicacion: {
        idExternoProvincia: ubicacion.idExternoProvincia ?? '',
        provincia: ubicacion.provincia || '',

        idExternoDepartamento: ubicacion.idExternoDepartamento ?? '',
        departamento: ubicacion.departamento || '',

        idExternoMunicipio: ubicacion.idExternoMunicipio ?? '',
        municipio: ubicacion.municipio || '',
        latitud: Number(formValue.ubicacion.lat),
        longitud: Number(formValue.ubicacion.lng),
      },
    };
    this.serviceAuth.postRegister(request).subscribe({
      next: (resp) => {

        this._status.set(ApiStatus.SUCCESS);
        this.router.navigate([`/${PUBLIC_ROUTES_ENUM.ROOT}/${PUBLIC_ROUTES_ENUM.LOGIN}`]);
        this.toastr.success('Se registró correctamente.', 'Éxito en el Registro');
      },
      error: (err) => {
        console.error('Login error:', err);
        this._status.set(ApiStatus.ERROR);
        this.toastr.error('Credenciales incorrectas.', 'Error de Autenticación');
      },
    });
  }
}
