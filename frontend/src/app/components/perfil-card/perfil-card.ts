import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  OnInit,
  output,
} from '@angular/core';
import { ButtonModule } from 'primeng/button';

import { UsuarioService } from '../../services/usuario.service';
import { ToastrService } from 'ngx-toastr';
import { FotoService } from '../../services/foto.service';
import { PerfilByUserStoreService } from '../../store/perfilByUser.stored.service';
import { UsuarioDetalleDTO } from '../../interfaces/UsuarioDetalleDTO.interface';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { SimpleLocationMap } from './simple-location-map/simple-location-map';

@Component({
  selector: 'app-perfil-card',
  imports: [ButtonModule, AvatarGroupModule, AvatarModule, SimpleLocationMap],
  templateUrl: './perfil-card.html',
  styleUrl: './perfil-card.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [UsuarioService, PerfilByUserStoreService],
})
export class PerfilCard implements OnInit {
  readonly servicioFoto = inject(FotoService);

  private readonly toastr = inject(ToastrService);

  usuario = input.required<UsuarioDetalleDTO>();

  hasFoto = computed(() => this.usuario().fotoLink);

  inicialesUsuario = computed(() => {
    const nombres = this.usuario().nombre.split(' ');
    const apellido = this.usuario().apellido.split(' ');

    const primerNombre = nombres[0] ? nombres[0][0] : '';
    const segundoNombre = apellido[0] ? apellido[0][0] : '';
    return (primerNombre + segundoNombre).toUpperCase();
  });

  ngOnInit(): void {}

}
