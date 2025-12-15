// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout-component/layout-component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { PublicacionCreateComponent } from './publicaciones/create/publicacion-create-component';
import { PublicacionDetailComponent } from './publicaciones/detail/publicacion-detail.component';
import { PublicacionEditComponent } from './publicaciones/edit/publicacion-edit-component';
import { PerfilSection } from '../common/perfil-section/perfil-section';
import { MapaGeograficoPublicaciones } from './mapa-geografico-publicaciones/mapa-geografico-publicaciones';

export const APP_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'publicaciones', pathMatch: 'full' },
      { path: 'perfil', component: PerfilSection },
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
      {
        path: 'mapa',
        component: MapaGeograficoPublicaciones,
      },
    ],
  },
];
