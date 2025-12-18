import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { Sidebar } from '../sidebar/sidebar';
import { ButtonModule } from 'primeng/button';
import { filter, map } from 'rxjs';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { SidebarMobile } from '../sidebar-mobile/sidebar-mobile';
import { NavigationUserComunService } from '../../store/navigationUserComun.service';
import { AuthStoreService } from '../../store/auth.stored.service';
@Component({
  selector: 'app-layout-component',
  imports: [RouterOutlet, SidebarMobile, Sidebar, ButtonModule],
  templateUrl: './layout-component.html',
  styleUrl: './layout-component.scss',
})
export class LayoutComponent {
  private breakpointObserver = inject(BreakpointObserver);
  readonly authStore = inject(AuthStoreService);
  // Creamos una señal reactiva que es true si la pantalla es > 768px (md de Tailwind)
  // 'min-width: 768px' coincide con el breakpoint 'md' de Tailwind
  isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 968px)').pipe(map((result) => result.matches)),
    { initialValue: false } // Valor inicial para evitar errores en SSR
  );
  // Drawer para mobile
  readonly drawerOpen = signal(false);

  openDrawer() {
    this.drawerOpen.set(true);
  }
  closeDrawer() {
    this.drawerOpen.set(false);
  }

  //-------------------------

  // Inyectamos nuestro nuevo servicio
  private navigationService = inject(NavigationUserComunService);

  // Mantenemos estos para la lógica visual (título, mostrar botón)
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  // ... (Tu código de breakpoints y drawer sigue igual) ...

  currentTitle = signal<string>('');
  showBackButton = signal<boolean>(false);

  ngOnInit() {
    // ... tu lógica de suscripción a eventos
    this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
      this.updateHeaderInfo();
    });

    this.updateHeaderInfo();
  }

  private updateHeaderInfo() {
    const currentUrl = this.router.url;

    // Tu lógica visual está perfecta aquí
    this.showBackButton.set(
      !currentUrl.endsWith('/publicaciones') && currentUrl !== '/app'
      // && currentUrl !== '/app/perfil'
    );

    let route = this.activatedRoute;
    while (route.firstChild) {
      route = route.firstChild;
    }
    const title = route.snapshot.data['title'] || '';
    this.currentTitle.set(title);
  }

  // AQUÍ ESTÁ LA MAGIA
  onBack() {
    if(this.authStore.isAdmin()){
      this.navigationService.goBack('/admin');
    }else{
      this.navigationService.goBack('/app');
    }

  }
}
