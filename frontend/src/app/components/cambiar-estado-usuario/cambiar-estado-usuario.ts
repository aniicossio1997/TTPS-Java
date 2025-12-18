import { Component, computed, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Button, ButtonModule } from 'primeng/button';
import { Select } from 'primeng/select';
import { EstadoUsuarioEnum } from '../../interfaces/local/estadoUsuarioEnum';
import { UsuarioDetalleDTO } from '../../interfaces/UsuarioDetalleDTO.interface';
import { UsuarioSmall } from '../../interfaces/usuario.interface.';
import { ApiStatus } from '../../interfaces/local/EnumApiStatus.enum';

interface IEstadosUsers{
     name: string;
    code: EstadoUsuarioEnum;
}
@Component({
  selector: 'app-cambiar-estado-usuario',
  imports: [ButtonModule, Select, FormsModule],
  templateUrl: './cambiar-estado-usuario.html',
  styleUrl: './cambiar-estado-usuario.scss',
})
export class CambiarEstadoUsuario {

  usuario = input.required<UsuarioDetalleDTO | UsuarioSmall>();
  onClose = output<void>();
  selectedEstado: { name: string; code: EstadoUsuarioEnum } | null = null;



  private readonly listaDeEstados: { name: string; code: EstadoUsuarioEnum }[] = [
    { name: 'Habilitar', code: EstadoUsuarioEnum.HABILITADO },
    { name: 'Bloquear', code: EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN },

  ];

  estadosAMostrar =computed(() => {
    return this.listaDeEstados.filter(
      (estado) => estado.code !== this.usuario().estado
    );
  })

  private _status = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError = computed(() => this._status() === ApiStatus.ERROR);
  onCloseLocal() {
    this.onClose.emit()
  }

  onConfirm(){

  }
}
