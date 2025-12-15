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

@Injectable()
export class ProvinciasStoreService {
  private readonly toastr = inject(ToastrService);
  readonly servicioUbicacion =inject(GeorefService)
  public provincias =signal<ProvinciaDTO[]>([])

  private _status  = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);


  public _getProvincias(){
    this._status.set(ApiStatus.LOADING);

    this.servicioUbicacion.getProvincias()
      .subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);
        this.provincias.set(resp.provincias)

      },
      error: (err) => {
        console.error('Login error:', err);
        this._status.set(ApiStatus.ERROR);
        this.toastr.error('Error al recuperar las fotos de la publicacion', 'Error en el servidor');
      }
    });
  }

}
