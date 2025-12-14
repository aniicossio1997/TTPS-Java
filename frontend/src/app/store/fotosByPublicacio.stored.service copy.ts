// src/app/store/auth.store.ts
import {
  Injectable,
  inject,
  signal,
  computed
} from '@angular/core';

import { ApiStatus } from '../interfaces/local/EnumApiStatus.enum';
import { ToastrService } from 'ngx-toastr';
import { UsuarioService } from '../services/usuario.service';
import { FotoService } from '../services/foto.service';
import { FotoLinkDTO } from '../interfaces/fotoLinkDTO';

@Injectable()
export class FotoByUserStoreService {
  private readonly toastr = inject(ToastrService);
  readonly servicioFoto =inject(FotoService)
  public fotoUsuario =signal<FotoLinkDTO | null>(null)

  private _status  = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);

  readonly servicePerfil =inject(UsuarioService)

  public _getFoto(idUsuario:number){
    this._status.set(ApiStatus.LOADING);

    this.servicioFoto.getFotoUsuario(idUsuario)
      .subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);
        this.fotoUsuario.set(resp)

      },
      error: (err) => {
        console.error('Login error:', err);
        this._status.set(ApiStatus.ERROR);
        this.toastr.error('Error al recuperar el perfil', 'Error en el servidor');
      }
    });
  }

}
