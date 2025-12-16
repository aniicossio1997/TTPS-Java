import { Routes } from '@angular/router';

import { LayoutComponent } from './layout/layout.component';
import { PublicacionesComponent } from '../common/publicaciones/publicaciones.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';

export enum PUBLIC_ROUTES_ENUM  {
  ROOT='public',
  INICIO='',
  REGISTER='register',
  LOGIN='login',
  PUBLICACIONES ='publicaciones'
}
// features/public/public.routes.ts
export const PUBLIC_ROUTES: Routes = [

  {
    path: '',
    component: LayoutComponent,

    children: [
    {
      path: PUBLIC_ROUTES_ENUM.INICIO,
      redirectTo: PUBLIC_ROUTES_ENUM.PUBLICACIONES,
      pathMatch: 'full',
    },
      { path: PUBLIC_ROUTES_ENUM.PUBLICACIONES, component: PublicacionesComponent},
      { path: PUBLIC_ROUTES_ENUM.REGISTER, component: RegisterComponent },
      { path: PUBLIC_ROUTES_ENUM.LOGIN, component: LoginComponent },
      //{ path: 'posts/:id', component: PostDetailPageComponent } // ver detalle de publicación
    ]
  }
];
