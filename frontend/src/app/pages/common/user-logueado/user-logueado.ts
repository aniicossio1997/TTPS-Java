import { Component, inject, input } from '@angular/core';
import { PerfilSection } from '../perfil-section/perfil-section';
import { AuthStoreService } from '../../../store/auth.stored.service';

@Component({
  selector: 'app-user-logueado',
  imports: [PerfilSection],
  templateUrl: './user-logueado.html',
  styleUrl: './user-logueado.scss',
})
export class UserLogueado {
  public authStore = inject(AuthStoreService);
}
