import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthStoreService } from '../store/auth.stored.service';
import { environment } from '../../environments/environment';


@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthStoreService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // Lista de endpoints que NO deben llevar Authorization
    const publicEndpoints = [
      `${environment.API_URL}/auth/register`,
      `${environment.API_URL}/auth/`,
      `${environment.API_URL}/public/publicaciones`,
      `${environment.API_URL}/public/`,
      `${environment.API_URL}/auth/login`,
      environment.URL_MAP  // por si querés mantenerlo
    ];

    // Si la URL matchea alguno de los endpoints públicos → no agregar Bearer
    const isPublic = publicEndpoints.some(url => request.url.startsWith(url));

    if (isPublic) {
      return next.handle(request);
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
