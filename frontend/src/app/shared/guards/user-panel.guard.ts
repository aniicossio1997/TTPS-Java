// guards/auth.guard.ts
import { inject } from '@angular/core';
import { CanMatchFn, Router } from '@angular/router';
import { AuthStoreService } from '../../store/auth.stored.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { map, take } from 'rxjs';

export const userPanelGuard: CanMatchFn = () => {
  const auth = inject(AuthStoreService);
  const router = inject(Router);


  if (!auth.getIsAuthenticated()) {
    return router.navigate(['/']);
  }

  if (auth.getIsAdmin()) {
    return router.navigate(['admin']);
  }



  return true;


};
