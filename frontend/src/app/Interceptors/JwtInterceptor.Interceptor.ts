import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthStoreService } from '../store/auth.stored.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authStore: AuthStoreService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const url = req.url;

    // Si usás proxy, tus llamadas deberían ser /api/...
    const isApi = url.includes('/api/');
    const isPublic =
      url.includes('/api/auth/') ||
      url.includes('/api/public/') ||
      url.includes('/api/georef'); // si lo exponés así por tu proxy

    if (!isApi || isPublic) {
      return next.handle(req);
    }

    const session = this.authStore.session();
    const token = session?.token;

    if (!token) {
      // te va a ayudar a ver por qué llega sin token
      return next.handle(req);
    }


    return next.handle(
      req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      })
    );
  }
}
