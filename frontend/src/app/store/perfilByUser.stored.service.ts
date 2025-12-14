// src/app/store/auth.store.ts
import {
  Injectable,
  inject,
  signal,
  computed,
  OnDestroy
} from '@angular/core';

import { AuthService } from '../services/auth.service';
import { LoginResponse, Usuario } from '../interfaces/LoginResponse.interface';
import { ApiStatus } from '../interfaces/local/EnumApiStatus.enum';
import { EnumRolUsuario } from '../interfaces/local/rol-usuario.enum';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UsuarioService } from '../services/usuario.service';
import { UsuarioDetalleDTO } from '../interfaces/UsuarioDetalleDTO.interface';
import { Subscription } from 'rxjs';

@Injectable()
export class PerfilByUserStoreService implements OnDestroy {

  private readonly toastr = inject(ToastrService);
  perfilDeUsuario= signal<UsuarioDetalleDTO | null>(null)

  private _status  = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);

  private subs=new Subscription()

  readonly servicePerfil =inject(UsuarioService)

  public _getPerfil(idUsuario:number, callback?:() => void){
    this._status.set(ApiStatus.LOADING);

    this.subs.add(this.servicePerfil.getUsuarioById(idUsuario).subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);
        this.perfilDeUsuario.set(resp)
        if(callback){
          callback()
        }

      },
      error: (err) => {
        console.error('Login error:', err);
        this._status.set(ApiStatus.ERROR);
        this.toastr.error('Error al recuperar el perfil', 'Error en el servidor');
      }
    }));

  }

  ngOnDestroy(): void {
   this.subs?.unsubscribe()
  }
}
