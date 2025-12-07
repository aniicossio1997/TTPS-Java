import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthStoreService } from '../store/user.stored.service';
import { environment } from '../../environments/environment';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthStoreService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

      // 1) Excluir URLs específicas
      const excludedApi = environment.API_URL;

      if (request.url.startsWith(excludedApi)) {
        return next.handle(request); // <-- NO agrega Authorization
      }

      // 2) Agregar token si corresponde
      const currentUser = this.authenticationService.getCurrentSession();

      if (currentUser?.token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${currentUser.token}`
          }
        });
      }

    return next.handle(request);
    }
}
