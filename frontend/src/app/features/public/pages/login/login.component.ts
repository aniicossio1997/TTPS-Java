import { Component, effect, inject, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
  FormsModule
} from '@angular/forms';

// PrimeNG
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FormControl } from '@angular/forms';
import { DividerModule } from 'primeng/divider';
import { RouterLink, Router } from '@angular/router';
import { MessageModule } from 'primeng/message';
import { AuthService } from '../../../../services/auth.service';
import { LoginResponse } from '../../../../interfaces/LoginResponse.interface';
import { AuthStoreService } from '../../../../store/auth.stored.service';
import { CommonModule } from '@angular/common';


interface Option {
  label: string;
  value: string;
}

type LoginForm = {
  email: FormControl<string>;
  password: FormControl<string>;
};

@Component({
  selector: 'app-login',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputTextModule,
    RouterLink,

    PasswordModule,
    ButtonModule,
    CardModule,
    PasswordModule,
    FormsModule,
    DividerModule,
    MessageModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {


  ngOnInit(): void {


  }

  loading = false;
  errorMessage = '';

  // 🔹 Inyectamos el FormBuilder con el nuevo patrón
  private readonly fb = inject(FormBuilder).nonNullable;
  private readonly router = inject(Router);
  public readonly authStore = inject(AuthStoreService);
  private readonly authService = inject(AuthService);

  // 🔹 Creamos el FormGroup con el método group() del FormBuilder
  //    Todos los controles son no-nullable gracias a .nonNullable
    readonly loginForm: FormGroup<LoginForm> = this.fb.group({

      email: this.fb.control('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required]),
    });

    constructor() {

    }

    onSubmit(): void {
      if (this.loginForm.invalid) {
        this.loginForm.markAllAsTouched();
        return;
      }

      this.loading = true;
      this.errorMessage = '';

      const { email, password } = this.loginForm.getRawValue();

      this.authStore.login(email, password)
    }

//     onSubmit(): void {
//   if (this.loginForm.invalid) {
//     this.loginForm.markAllAsTouched();
//     return;
//   }

//   this.loading = true;
//   this.errorMessage = '';

//   const { email, password } = this.loginForm.getRawValue();

//   this.authService.login(email, password!).subscribe({
//     next: (response: LoginResponse) => {
//       this.authService.guardarSesion(response);

//       const rol = response.usuario.rol;

//       if (rol === 'ADMINISTRADOR') {
//         this.router.navigate(['/admin']); // administrador
//       } else {
//         this.router.navigate(['/app']); // usuario normal
//       }
//     },
//     error: (err) => {
//       console.error('Error en login', err);
//       if (err.status === 401 || err.status === 400) {
//         this.errorMessage = 'Credenciales incorrectas.';
//       } else {
//         this.errorMessage = 'Ocurrió un error al iniciar sesión. Intenta nuevamente.';
//       }
//       this.loading = false;
//     },
//     complete: () => {
//       this.loading = false;
//     }
//   });
// }



// helper (mucho más cómodo de usar en HTML)

    isInvalid(controlName: string) {
        const control = this.loginForm.get(controlName);
        return control?.invalid && (control.touched || control.dirty);
    }

}
