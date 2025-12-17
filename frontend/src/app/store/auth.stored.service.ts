// src/app/store/auth.store.ts
import {
  Injectable,
  inject,
  signal,
  computed
} from '@angular/core';

import { AuthService } from '../services/auth.service';
import { LoginResponse } from '../interfaces/LoginResponse.interface';
import { EnumRolUsuario } from '../interfaces/local/rol-usuario.enum';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UsuarioSmall } from '../interfaces/usuario.interface.';

@Injectable({ providedIn: 'root' })
export class AuthStoreService {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toastr = inject(ToastrService);
  // ===== Signals privados =====
  private readonly _session = signal<LoginResponse | null>(null);

  // ===== Estado público de solo lectura =====
  readonly session = this._session.asReadonly();

  // ===== Computed =====
  readonly usuario   = computed<UsuarioSmall | null>(() => this._session()?.usuario ?? null);

  readonly token     = computed<string | null>(() => this._session()?.token ?? null);


  readonly isAuthenticated = computed(() => !!this.token());

  readonly isAdmin = computed(() => this.usuario()?.rol == EnumRolUsuario.ADMINISTRADOR);

  readonly isUserComun  = computed(
    () => this.isAuthenticated() && this.usuario()?.rol == EnumRolUsuario.USUARIO_COMUN
  );

  constructor() {
    this.restoreFromStorage();
  }

  onSaveSesionError(error?:any){
    this._session.set(null);
    this.clearStorage();
    this.toastr.error('Credenciales incorrectas.', 'Error de Autenticación');
  }

  // ===== Acciones públicas =====

  // ===== LOGIN SIN await (solo subscribe) =====
  onSaveSession(sesion:LoginResponse): void {
      this._session.set(sesion);
      this.saveToStorage(sesion);

      if (this.isAdmin()) {
        this.router.navigate(['/admin']);
        return;
      }

      if (this.isUserComun()) {
        this.router.navigate(['/app']);
        return;
      }

  }


  logout(): void {
    this._session.set(null);
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
      const usuario: UsuarioSmall = JSON.parse(rawUsuario);
      this._session.set({ token, usuario });
    } catch {
      this._session.set(null);
      this.clearStorage();
    }
  }

  updateUsuarioEnSesion(usuario: UsuarioSmall) {

    const current = this._session();
    if (!current) return;

    if(current?.usuario.id != usuario.id){
      return;
    }

  const nextUsuario: UsuarioSmall = {
    ...current.usuario,   // lo viejo
    ...usuario,             // lo nuevo
  };

  const next: LoginResponse = {
    ...current,
    usuario: { ...nextUsuario },
  };

    this._session.set(next);
    localStorage.setItem('usuario', JSON.stringify(this._session()!.usuario!));
  }



}
