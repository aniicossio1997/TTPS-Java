import { ChangeDetectionStrategy, Component, computed, inject, input, OnInit, output } from '@angular/core';
import { ButtonModule } from 'primeng/button';

import { MenuItem } from 'primeng/api';
import { UsuarioService } from '../../services/usuario.service';
import { ToastrService } from 'ngx-toastr';
import { FotoService } from '../../services/foto.service';
import { PerfilByUserStoreService } from '../../store/perfilByUser.stored.service';



@Component({
  selector: 'app-perfil-card',
  imports: [ButtonModule],
  templateUrl: './perfil-card.html',
  styleUrl: './perfil-card.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers:[UsuarioService, PerfilByUserStoreService]
})
export class PerfilCard implements OnInit {

  readonly perfilStore =inject(PerfilByUserStoreService)
  readonly servicioFoto =inject(FotoService)


  private readonly toastr = inject(ToastrService);

  onEditarPerfil = output<void>();
  onEditarPassword = output<void>();

  idUsuario =input.required<number>()

  perfilDeUsuario= computed(()=> this.perfilStore.perfilDeUsuario())

  items: MenuItem[] = [
    {
      label: 'Editar Perfil',
      icon: 'pi pi-pencil',
      styleClass: 'text-sm ',
      command: () =>{
         this.onEditarPerfil.emit()
      },
    },
    {
      label: 'Cambiar contraseña',
      icon: 'pi pi-key',
      styleClass: 'text-sm ',

      command: () => {
        this.onEditarPassword.emit()
      },
    },
  ];



  ngOnInit(): void {
    this.perfilStore._getPerfil(this.idUsuario())

  }




  onLocalIrCambiarPassword(){
    this.onEditarPassword.emit()
  }

}
