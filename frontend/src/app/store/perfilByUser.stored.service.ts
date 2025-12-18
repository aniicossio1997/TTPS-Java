// src/app/store/auth.store.ts
import { Injectable, inject, signal, computed, OnDestroy } from '@angular/core';

import { ApiStatus } from '../interfaces/local/EnumApiStatus.enum';
import { ToastrService } from 'ngx-toastr';
import { UsuarioService } from '../services/usuario.service';
import { UsuarioDetalleDTO } from '../interfaces/UsuarioDetalleDTO.interface';
import { Subscription } from 'rxjs';
import { EstadoUsuarioEnum } from '../interfaces/local/estadoUsuarioEnum';

@Injectable()
export class PerfilByUserStoreService implements OnDestroy {
  private readonly toastr = inject(ToastrService);
  perfilDeUsuario = signal<UsuarioDetalleDTO | null>(null);

  private _status = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError = computed(() => this._status() === ApiStatus.ERROR);

  private subs = new Subscription();

  getColorEstado = computed <'warn' | 'success' | 'secondary' | 'info' | 'danger' | 'contrast'
      >(() => {

            const estado = this.perfilDeUsuario()?.estado;
            switch (estado) {
              case EstadoUsuarioEnum.BAJA_VOLUNTARIA:
                return 'danger';
              case EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN:
                return 'danger';
              case EstadoUsuarioEnum.HABILITADO:
                return 'success'
              default:
                return 'secondary';
            }
      })

      getLabelEstadoUsuario = computed <'Baja voluntaria' | 'Bloqueado' | 'Habilitado' | 'Desconocido'
      >(() => {

            const estado = this.perfilDeUsuario()?.estado;
          switch (estado) {
            case EstadoUsuarioEnum.BAJA_VOLUNTARIA:
              return 'Baja voluntaria';
            case EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN:
              return 'Bloqueado';
            default:
              return 'Habilitado';
          }
      })




  readonly servicePerfil = inject(UsuarioService);

  public _getPerfil(idUsuario: number, callback?: (e?:any) => void) {
    this._status.set(ApiStatus.LOADING);

    this.subs.add(
      this.servicePerfil.getUsuarioById(idUsuario).subscribe({
        next: (resp) => {
          this._status.set(ApiStatus.SUCCESS);
          this.perfilDeUsuario.set(resp);
          if (callback) {
            callback?.(resp);
          }
        },
        error: (err) => {
          console.error('Login error:', err);
          this._status.set(ApiStatus.ERROR);
          this.toastr.error('Error al recuperar el perfil', 'Error en el servidor');
        },
      })
    );
  }


  ngOnDestroy(): void {
    this.subs?.unsubscribe();
  }
}
