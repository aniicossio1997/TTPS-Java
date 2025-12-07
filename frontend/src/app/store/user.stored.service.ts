// src/app/store/auth.store.ts
import {
  Injectable,
  inject,
  signal,
  computed,
  PLATFORM_ID
} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../services/auth.service';
import { LoginResponse, Usuario } from '../interfaces/LoginResponse.interface';
import { ApiStatus } from '../interfaces/local/EnumApiStatus.enum';
import { EnumRolUsuario } from '../interfaces/local/rol-usuario.enum';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable({ providedIn: 'root' })
export class AuthStoreService {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toastr = inject(ToastrService);
  // ===== Signals privados =====
  private readonly _session = signal<LoginResponse | null>(null);
  private readonly _status  = signal<ApiStatus>(ApiStatus.INIT);

  // ===== Estado público de solo lectura =====
  readonly session = this._session.asReadonly();
  readonly status  = this._status.asReadonly();

  // ===== Computed =====
  readonly usuario   = computed<Usuario | null>(() => this._session()?.usuario ?? null);

  readonly token     = computed<string | null>(() => this._session()?.token ?? null);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);

  readonly isAuthenticated = computed(() => !!this.token());

  readonly isAdmin = computed(() => this.usuario()?.rol == EnumRolUsuario.ADMINISTRADOR);

  readonly isUserComun  = computed(
    () => this.isAuthenticated() && this.usuario()?.rol == EnumRolUsuario.USUARIO_COMUN
  );

  constructor() {
    this.restoreFromStorage();
  }

  // ===== Acciones públicas =====

  // ===== LOGIN SIN await (solo subscribe) =====
  login(email: string, password: string): void {
    this._status.set(ApiStatus.LOADING);

    this.authService.login(email, password).subscribe({
      next: (resp) => {
        console.log('Login successful:', resp);
        this._session.set(resp);
        this._status.set(ApiStatus.SUCCESS);
        this.saveToStorage(resp);
         // ✅ RE-INTRODUCIR LA NAVEGACIÓN AQUÍ.
              // La navegación se dispara inmediatamente después de actualizar la señal.
              if (this.isAdmin()) {
                this.router.navigate(['/admin']);
                return;
              }
              if (this.isUserComun()) {
                this.router.navigate(['/app']);
                return;
              }

      },
      error: (err) => {
        console.error('Login error:', err);
        this._session.set(null);
        this._status.set(ApiStatus.ERROR);
        this.clearStorage();
        this.toastr.error('Credenciales incorrectas.', 'Error de Autenticación');
      }
    });
  }

  logout(): void {
    this._session.set(null);
    this._status.set(ApiStatus.INIT);
    this.clearStorage();
    this.router.navigate(['/public']);
  }

  getIsAuthenticated() {
    return !!this.token();
  }

  getIsAdmin() {
    return this.usuario()?.rol == EnumRolUsuario.ADMINISTRADOR;
  }

  getIsUserComun() {
    return this.getIsAuthenticated() && this.usuario()?.rol == EnumRolUsuario.USUARIO_COMUN;
  }
  getCurrentSession(){
    return this.session();
  }

  // ===== Helpers privados =====

  private saveToStorage(resp: LoginResponse): void {

    console.log('Saving to storage:', resp);
    console.log('Token:', resp.token);
    console.log('Usuario:', resp.usuario);
    localStorage.setItem('token', resp.token);
    localStorage.setItem('usuario', JSON.stringify(resp.usuario));
  }

  private clearStorage(): void {

    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  private restoreFromStorage(): void {


    const token = localStorage.getItem('token');
    const rawUsuario = localStorage.getItem('usuario');

    if (!token || !rawUsuario) {
      this._session.set(null);
      return;
    }

    try {
      const usuario: Usuario = JSON.parse(rawUsuario);
      this._session.set({ token, usuario });
      this._status.set(ApiStatus.SUCCESS);
    } catch {
      this._session.set(null);
      this.clearStorage();
    }
  }
}
