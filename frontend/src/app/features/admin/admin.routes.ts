import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './adminLayout/adminLayout.component';
import { AdminUsersComponent } from './adminUsers/adminUsers.component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { PerfilComponent } from '../common/perfil/perfil.component';


export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      { path: 'users', component: AdminUsersComponent },
      { path: 'publicaciones', component: PublicacionesComponent },
      { path: 'perfil', component: PerfilComponent },
    ]
  }
];
