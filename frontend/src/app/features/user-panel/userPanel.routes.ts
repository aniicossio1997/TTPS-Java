// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout-component/layout-component';
import { PerfilComponent } from '../common/perfil/perfil.component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { PublicacionFormComponent } from './publicaciones/form/publicacion-form/publicacion-form';


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
        component: PublicacionFormComponent,
      },
    ],
  },
];
