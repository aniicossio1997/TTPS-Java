import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { LayoutComponent } from './layout/layout.component';

// features/public/public.routes.ts
export const PUBLIC_ROUTES: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: '', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      //{ path: 'posts/:id', component: PostDetailPageComponent } // ver detalle de publicación
    ]
  }
];
