// guards/admin.guard.ts
import { inject } from '@angular/core';
import { CanMatchFn, Router } from '@angular/router';
import { AuthStoreService } from '../../store/user.stored.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { map, take } from 'rxjs';

export const adminGuard: CanMatchFn = () => {
  const auth = inject(AuthStoreService);
  const router = inject(Router);

  console.log('ADMIN GUARD EJECUTADO...', auth.session());

  if (!auth.getIsAuthenticated()) {
    return router.navigate(['/']);
  }

  if (auth.getIsUserComun()) {
    return router.navigate(['app']);
  }

  return true;


};

