import { Injectable, inject, signal } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';
import { filter } from 'rxjs';


type Prefix = '/app' | '/admin';


@Injectable({
  providedIn: 'root'
})
export class NavigationUserComunService {
 private router = inject(Router);
  private location = inject(Location);

  private history: string[] = [];

  constructor() {
    this.router.events
      .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
      .subscribe((event) => {
        this.history.push(event.urlAfterRedirects);
      });
  }

  goBack(prefix: Prefix): void {
    const currentUrl = this.router.url;

    const HOME = `${prefix}/publicaciones`;
    const PERFIL = `${prefix}/perfil`;
    const MAPA = `${prefix}/mapa`;
    const CREAR = `${prefix}/publicaciones/crear`;
    const LISTA_USUARIOS = `${prefix}/usuarios`;
    const DETALLE_USUARIO = `${prefix}/usuarios/`;

    // ✅ mapa y venía de crear/editar => volver a listado
    if (this.isMapaUrl(currentUrl, MAPA) && this.wasPreviousUrlEditOrNew(prefix)) {
      this.router.navigate([HOME]);
      return;
    }

    // ✅ perfil y venía de crear => volver a listado
    if (this.isPerfilUrl(currentUrl, PERFIL) && this.wasPreviousUrlNewPublicacion(CREAR)) {
      this.router.navigate([HOME]);
      return;
    }

    // ✅ detalle y venía de editar/crear => ir al perfil (tu regla)
    if (this.isDetailUrl(currentUrl) && this.wasPreviousUrlEditOrNew(prefix)) {
      this.router.navigate([PERFIL]);
      return;
    }

    // ✅ crear/editar => volver a listado
    if (currentUrl.includes('/editar') || currentUrl.includes('/crear')) {
      this.router.navigate([HOME]);
      return;
    }

        // ✅ ver detalle de usuario => volver a listado de usuario
    if (currentUrl.includes('/usuarios/')) {
      this.router.navigate([LISTA_USUARIOS]);
      return;
    }



    // ✅ default
    if (this.history.length > 1) {
      this.location.back();
    } else {
      this.router.navigate([HOME]);
    }
  }

  // -------- Helpers --------

  private isDetailUrl(url: string): boolean {
    return url.includes('/publicaciones/detalle/');
  }

  private isPerfilUrl(url: string, perfilUrl: string): boolean {
    return url.startsWith(perfilUrl);
  }

  private isMapaUrl(url: string, mapaUrl: string): boolean {
    return url.startsWith(mapaUrl);
  }

  private prev(): string | undefined {
    return this.history[this.history.length - 2];
  }

  private wasPreviousUrlEditOrNew(prefix: Prefix): boolean {
    const previous = this.prev();
    if (!previous) return false;

    // opcional pero recomendado: que sea del mismo módulo (/app o /admin)
    if (!previous.startsWith(prefix)) return false;

    return previous.includes('/editar/') || previous.includes('/crear');
  }

  private wasPreviousUrlNewPublicacion(crearUrl: string): boolean {
    const previous = this.prev();
    return !!previous && previous.startsWith(crearUrl);
  }

}
