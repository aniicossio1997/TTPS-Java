import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
  FormsModule,
  ValidationErrors
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
import { LocationPickerComponent } from '../../../../components/LocationPicker/location-picker.component';
import { CommonModule } from '@angular/common';
import { Dialog } from 'primeng/dialog';
import { UbicacionExternaResponse } from '../../../../interfaces/ubicacionExternaResponse';
import { AuthService } from '../../../../services/auth.service';
import { ApiStatus } from '../../../../interfaces/local/EnumApiStatus.enum';
import { RegisterRequest } from '../../../../interfaces/registerRequest.interface';
import { EnumRolUsuario } from '../../../../interfaces/local/rol-usuario.enum';
import { ToastrService } from 'ngx-toastr';


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

function ubicacionRequeridaValidator(control: AbstractControl): ValidationErrors | null {
  const value = control.value as { lat?: string; lng?: string } | null;

  if (!value) {
    return { required: true };
  }

  const lat = value.lat?.toString().trim();
  const lng = value.lng?.toString().trim();

  if (!lat || !lng) {
    return { required: true };
  }

  return null;
}

interface Option {
  label: string;
  value: string;
}

type RegisterForm = {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  phone: FormControl<string>;
    ubicacion: FormGroup<{
    lat: FormControl<string>;
    lng: FormControl<string>;
  }>;
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

    LocationPickerComponent
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  schemas:[]
})
export class RegisterComponent implements OnInit {
  private readonly router = inject(Router);
  readonly serviceAuth = inject(AuthService);
  private readonly toastr = inject(ToastrService);
  private readonly _status  = signal<ApiStatus>(ApiStatus.INIT);

  ubicacionExterna= signal<UbicacionExternaResponse|null>(null);

  onLocationSelected(coords: { lat: number; lng: number, ubicacionExterna: UbicacionExternaResponse }) {
    console.log('Ubicación seleccionada en el registro:', coords);
    this.registerForm.controls['ubicacion'].setValue({ lat: coords.lat.toString(), lng: coords.lng.toString() });
    this.ubicacionExterna.set(coords.ubicacionExterna);
   // this.visibleMapa = false;
    // acá podés guardar en el formulario, mandar al backend, etc.
  }


  ngOnInit(): void {

  }

  // 🔹 Inyectamos el FormBuilder con el nuevo patrón
  private readonly fb = inject(FormBuilder).nonNullable;

  // 🔹 Creamos el FormGroup con el método group() del FormBuilder
  //    Todos los controles son no-nullable gracias a .nonNullable
  readonly registerForm: FormGroup<RegisterForm> = this.fb.group({
    firstName: this.fb.control('', [Validators.required]),
    lastName: this.fb.control('', [Validators.required]),
    email: this.fb.control('', [Validators.required, Validators.email]),
    phone: this.fb.control(''),
      ubicacion: this.fb.group({
    lat: this.fb.control('', [Validators.required]),
    lng: this.fb.control('', [Validators.required]),
  }),
    password: this.fb.control('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: this.fb.control('', [Validators.required])
  },
  { validators: passwordMatchValidator }
);


  onSubmit(): void {


    this.registerForm.markAllAsTouched();
    console.log('Submitting register form', this.registerForm.invalid);
    if (this.registerForm.invalid) {
        this.registerForm.markAllAsTouched();
        console.log('❌ Errores del formulario:', this.registerForm.errors);
        console.log('❌ Errores por control:', this.registerForm.controls);
    }

    // getRawValue() para mantener bien el tipo
    const data = this.registerForm.getRawValue();
    console.log('Form value', data);
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


    hasUbicacionExterna(): boolean {
      const ubicacion = this.registerForm.get('ubicacion')?.value;

      return !!ubicacion
        && ubicacion.lat !== ''
        && ubicacion.lng !== '';
    }

    private _onRegister(){
      this._status.set(ApiStatus.LOADING);
      const formValue = this.registerForm.getRawValue();

      const request:RegisterRequest={
        nombre: formValue.firstName,
        apellido: formValue.lastName,
        email: formValue.email,
        password: formValue.password,
        rol: EnumRolUsuario.USUARIO_COMUN,
        ubicacion: {
          idExterno: this.ubicacionExterna()!.ubicacion.departamento.id ?? '',
          provincia: this.ubicacionExterna()!.ubicacion.provincia.nombre || '',
          ciudad: this.ubicacionExterna()!.ubicacion.municipio.nombre || '',
          barrio: this.ubicacionExterna()!.ubicacion.departamento.nombre || '',
          latitud: Number(formValue.ubicacion.lat),
          longitud: Number(formValue.ubicacion.lng),

        }
      }
      this.serviceAuth.postRegister(request).subscribe({
            next: (resp) => {
              console.log('Login successful:', resp);
              this._status.set(ApiStatus.SUCCESS);
              this.router.navigate(['/']);
              this.toastr.success('Inicie sesion.', 'Exito');

            },
            error: (err) => {
              console.error('Login error:', err);
              this._status.set(ApiStatus.ERROR);
              this.toastr.error('Credenciales incorrectas.', 'Error de Autenticación');
            }
      });
    }

}



