import { Routes } from '@angular/router';
import { publicGuard } from './shared/guards/public.guard';
import { userPanelGuard } from './shared/guards/user-panel.guard';
import { adminGuard } from './shared/guards/admin.guard';
import { NotFound } from './features/not-found/not-found';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'public',
    pathMatch: 'full',
  },
  {
    path: 'public',
    canMatch: [publicGuard],
    loadChildren: () =>
      import('./features/public/public.routes').then(m => m.PUBLIC_ROUTES)
  },
  {
    path: 'app',
    canMatch: [userPanelGuard],
    loadChildren: () =>
      import('./features/user-panel/userPanel.routes').then(m => m.APP_ROUTES)
  },
  {
    path: 'admin',
    canMatch: [adminGuard],
    loadChildren: () =>
      import('./features/admin/admin.routes').then(m => m.ADMIN_ROUTES)
  },


  // 404
  { path: '**', component: NotFound}
];
