// src/app/store/auth.store.ts
import {
  Injectable,
  inject,
  signal,
  computed
} from '@angular/core';

import { ApiStatus } from '../interfaces/local/EnumApiStatus.enum';
import { ToastrService } from 'ngx-toastr';
import { GeorefService } from '../services/georef.service';
import { ProvinciaDTO } from '../interfaces/georef/provinciaDTO';
import { UsuarioService } from '../services/usuario.service';
import { EstadoUsuarioEnum } from '../interfaces/local/estadoUsuarioEnum';

@Injectable()
export class CambiarEstadoUsuarioStoreService {
  private readonly toastr = inject(ToastrService);
  readonly usuarioService =inject(UsuarioService)
  public provincias =signal<any>(null)

  private _status  = signal<ApiStatus>(ApiStatus.INIT);

  private mensajeError = signal<string>('')
  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);

  readonly mensajeErrorComputed = computed(() => this.mensajeError());

  public _cambiarEstadoUsuario(idUsuario:number, nuevoEstado:EstadoUsuarioEnum,  callback?: (e?:any) => void){
    this._status.set(ApiStatus.LOADING);

    this.usuarioService.pathEstadoUsuario(idUsuario,nuevoEstado)
      .subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);

        if (callback) {
          callback?.(resp);
        }
      },
      error: (err) => {
        console.log(err);
      let errorMsj =err.error?.message ?? 'No se pudo cambiar el estado del usuario';
      this.mensajeError.set(errorMsj)
      this._status.set(ApiStatus.ERROR)
      this.toastr.error(errorMsj, 'Error ');
      }
    });
  }

}
