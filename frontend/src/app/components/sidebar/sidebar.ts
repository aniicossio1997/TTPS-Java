import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ButtonModule } from 'primeng/button';

import { AuthStoreService } from '../../store/auth.stored.service';

@Component({
  selector: 'app-sidebar',
  imports: [
    RouterLink, RouterLinkActive, ButtonModule
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {

  readonly authStore = inject(AuthStoreService);


  salir(){
    console.log('Cerrando sesión desde el sidebar');
    this.authStore.logout();
  }
}
