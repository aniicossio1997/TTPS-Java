// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout-component/layout-component';
import { PerfilSection } from '../common/perfil-section/perfil-section';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';


export const APP_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'publicaciones', pathMatch: 'full' },
      { path: 'perfil', component: PerfilSection },
      { path: 'publicaciones', component: PublicacionesComponent },
    ]
  }
];
