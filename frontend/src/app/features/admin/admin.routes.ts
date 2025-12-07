import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './adminLayout/adminLayout.component';
import { AdminUsersComponent } from './adminUsers/adminUsers.component';


export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      { path: 'users', component: AdminUsersComponent },

    ]
  }
];
