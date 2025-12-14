// guards/public.guard.ts
import { inject } from '@angular/core';
import { CanMatchFn, Router } from '@angular/router';
import { AuthStoreService } from '../../store/auth.stored.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { map, take } from 'rxjs';

export const publicGuard: CanMatchFn = () => {
  const auth = inject(AuthStoreService);
  const router = inject(Router);

  // Si es admin, va a admin
  if (auth.getIsAdmin()) {
    return router.navigate(['/admin']);
  }

  // Si es usuario común, va a app
  if (auth.getIsUserComun()) {
    return router.navigate(['/app']);
  }

  // Si no está autenticado, permite acceso a login/register
  return true;

};
