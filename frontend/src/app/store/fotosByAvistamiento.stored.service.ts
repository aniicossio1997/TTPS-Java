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
export class FotosByAvistamientoStoreService {
  private readonly toastr = inject(ToastrService);
  readonly servicioFoto =inject(FotoService)
  public fotos =signal<FotoLinkDTO[]>([])

  private _status  = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);


  public _getFoto(publicacionId:number){
    this._status.set(ApiStatus.LOADING);

    this.servicioFoto.getFotosByPublicacion(publicacionId)
      .subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);
        this.fotos.set(resp)

      },
      error: (err) => {
        console.error('Login error:', err);
        this._status.set(ApiStatus.ERROR);
        this.toastr.error('Error al recuperar las fotos del avistamiento', 'Error en el servidor');
      }
    });
  }

}
