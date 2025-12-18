import { Component, computed, effect, inject, OnInit, signal } from '@angular/core';
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
import { AuthService } from '../../../services/auth.service';
import { AuthStoreService } from '../../../store/auth.stored.service';
import { CommonModule } from '@angular/common';
import { ApiStatus } from '../../../interfaces/local/EnumApiStatus.enum';
import { HttpErrorResponse } from '@angular/common/http';


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

  errorMessage = '';

  // 🔹 Inyectamos el FormBuilder con el nuevo patrón
  private readonly fb = inject(FormBuilder).nonNullable;
  private readonly router = inject(Router);
  public readonly authStore = inject(AuthStoreService);
  private readonly authService = inject(AuthService);


  private readonly _status  = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);

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

      this.errorMessage = '';

      const { email, password } = this.loginForm.getRawValue();

      this.onLogin(email, password)
    }

  // ===== LOGIN SIN await (solo subscribe) =====
  onLogin(email: string, password: string): void {
    this._status.set(ApiStatus.LOADING);

    this.authService.login(email, password).subscribe({
      next: (resp) => {

        this.authStore.onSaveSession(resp)
        this._status.set(ApiStatus.SUCCESS);
      },
      error: (err : HttpErrorResponse) => {
        let mensjae = err.error.message || 'Error en el Correo o Contraseña'
        this.errorMessage = mensjae;
        console.log('Login error:', err.error.message);
        this._status.set(ApiStatus.ERROR);
        this.authStore.onSaveSesionError(mensjae)
      }
    });
  }
    isInvalid(controlName: string) {
        const control = this.loginForm.get(controlName);
        return control?.invalid && (control.touched || control.dirty);
    }

}
