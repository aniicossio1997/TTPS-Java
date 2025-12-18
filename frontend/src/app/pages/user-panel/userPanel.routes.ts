// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent } from '../../components/layout-component/layout-component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { PublicacionCreateComponent } from './publicaciones/create/publicacion-create-component';
import { PublicacionDetailComponent } from './publicaciones/detail/publicacion-detail.component';
import { PublicacionEditComponent } from './publicaciones/edit/publicacion-edit-component';
import { PerfilSection } from '../common/perfil-section/perfil-section';
import { MapaGeograficoPublicaciones } from '../common/mapa-geografico-publicaciones/mapa-geografico-publicaciones';
import { UserLogueado } from '../common/user-logueado/user-logueado';


export const APP_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'publicaciones', pathMatch: 'full' },
      { path: 'publicaciones', component: PublicacionesComponent, data: { title: 'Inicio' } },
      {
        path: 'publicaciones/crear',
        component: PublicacionCreateComponent,
        data: { title: 'Nueva Publicación',backStrategy: 'history' }
      },
      {
        path: 'publicaciones/detalle/:id',
        component: PublicacionDetailComponent,
        data: { title: 'Detalle de Publicación',backStrategy: 'history' }
      },
      {
        path: 'publicaciones/editar/:id',
        component: PublicacionEditComponent,
        data: { title: 'Editar Publicación' }
      },
      {
        path: 'mapa',
        component: MapaGeograficoPublicaciones,
        data: { title: 'Mapa de Avistamientos' }
      },
      { path: 'perfil', component: UserLogueado ,data: { title: 'Mi Perfil' }},


    ],
  },
];
