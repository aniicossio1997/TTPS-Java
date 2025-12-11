// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { LayoutComponent } from './layout-component/layout-component';
import { PerfilComponent } from '../common/perfil/perfil.component';


export const APP_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'perfil', pathMatch: 'full' },
      { path: 'perfil', component: PerfilComponent },

    ]
  }
];
