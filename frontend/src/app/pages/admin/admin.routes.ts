import { Routes } from '@angular/router';
import { AdminUsersComponent } from './adminUsers/adminUsers.component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { PerfilSection } from '../common/perfil-section/perfil-section';
import { LayoutComponent } from '../../components/layout-component/layout-component';
import { PublicacionCreateComponent } from '../user-panel/publicaciones/create/publicacion-create-component';
import { PublicacionDetailComponent } from '../user-panel/publicaciones/detail/publicacion-detail.component';
import { PublicacionEditComponent } from '../user-panel/publicaciones/edit/publicacion-edit-component';
import { MapaGeograficoPublicaciones } from '../common/mapa-geografico-publicaciones/mapa-geografico-publicaciones';
import { UserLogueado } from '../common/user-logueado/user-logueado';


export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'publicaciones', pathMatch: 'full' },

      { path: 'usuarios', component: AdminUsersComponent, data: { title: 'usuarios' } },

      { path: 'usuarios/:usuarioId', component: PerfilSection ,data: { title: 'Perfil de otro usuario' }},
      {
        path: 'mapa',
        component: MapaGeograficoPublicaciones,
        data: { title: 'Mapa de Avistamientos' }
      },

      // RUTA 1: Mi Perfil (Sin ID, asume el usuario logueado)
      {
        path: 'perfil',
        component: UserLogueado,
        data: { title: 'Mi Perfil', isOwnProfile: true }
      },


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

    ]
  }
];
