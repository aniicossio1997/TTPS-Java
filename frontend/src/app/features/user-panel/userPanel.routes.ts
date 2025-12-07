// features/app/app.routes.ts
import { Routes } from '@angular/router';
import { PerfilComponent } from './perfil-component/perfil-component';
import { LayoutComponent } from './layout-component/layout-component';


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
