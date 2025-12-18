import { ChangeDetectionStrategy, Component, computed, inject, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ButtonModule } from 'primeng/button';

import { AuthStoreService } from '../../store/auth.stored.service';
import { UserAvatarComponent } from '../user-avatar/user-avatar';
import { UsuarioService } from '../../services/usuario.service';
import { CommonModule } from '@angular/common';
import { AvatarModule } from 'primeng/avatar';
import { EnumRolUsuario } from '../../interfaces/local/rol-usuario.enum';

@Component({
  selector: 'app-sidebar',
  imports: [
    RouterLink, RouterLinkActive, ButtonModule, UserAvatarComponent, CommonModule, AvatarModule
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
  providers:[UsuarioService],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Sidebar implements OnInit {
  readonly authStore = inject(AuthStoreService);


  usuario= computed(()=> this.authStore.usuario())
  isAdmin= computed(()=> this.authStore.usuario()?.rol==EnumRolUsuario.ADMINISTRADOR)

  constructor(){}



  ngOnInit(): void {

  }

  salir(){

    this.authStore.logout();
  }


}
