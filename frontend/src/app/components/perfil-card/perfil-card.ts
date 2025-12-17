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

@Component({
  selector: 'app-perfil-card',
  imports: [ButtonModule,],
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

  ngOnInit(): void {}
}
