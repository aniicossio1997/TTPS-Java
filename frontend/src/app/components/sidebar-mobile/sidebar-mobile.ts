import { Component, computed, DestroyRef, inject, input, output } from '@angular/core';
import { Sidebar } from '../sidebar/sidebar';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { NavigationEnd, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthStoreService } from '../../store/auth.stored.service';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { EnumRolUsuario } from '../../interfaces/local/rol-usuario.enum';

@Component({
  selector: 'app-sidebar-mobile',
  imports: [ DrawerModule, ButtonModule,RouterLink, RouterLinkActive],
  templateUrl: './sidebar-mobile.html',
  styleUrl: './sidebar-mobile.scss',
})
export class SidebarMobile {

  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  readonly authStore = inject(AuthStoreService);
  drawerOpen = input.required<boolean>()

  closeDrawer =  output<void>();

  usuario= computed(()=> this.authStore.usuario())
  isAdmin= computed(()=> this.authStore.usuario()?.rol==EnumRolUsuario.ADMINISTRADOR)
  constructor(){

  }

  salir(){

    this.authStore.logout();
  }
}
