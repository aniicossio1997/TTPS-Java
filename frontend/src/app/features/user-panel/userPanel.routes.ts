// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout-component/layout-component';
import { PerfilComponent } from '../common/perfil/perfil.component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { PublicacionCreateComponent } from './publicaciones/create/publicacion-create-component';
import { PublicacionDetailComponent } from './publicaciones/detail/publicacion-detail.component';
import { PublicacionEditComponent } from './publicaciones/edit/publicacion-edit-component';

export const APP_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'perfil', pathMatch: 'full' },
      { path: 'perfil', component: PerfilComponent },
      { path: 'publicaciones', component: PublicacionesComponent },
      {
        path: 'publicaciones/crear',
        component: PublicacionCreateComponent,
      },
      {
        path: 'publicaciones/detalle/:id',
        component: PublicacionDetailComponent,
      },
      {
        path: 'publicaciones/editar/:id',
        component: PublicacionEditComponent,
      },
    ],
  },
];
