import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../../../components/sidebar/sidebar';
import { DrawerModule } from 'primeng/drawer';
import { ButtonModule } from 'primeng/button';
import { distinctUntilChanged, map } from 'rxjs';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { SidebarMobile } from '../../../components/sidebar-mobile/sidebar-mobile';
@Component({
  selector: 'app-layout-component',
  imports: [RouterOutlet,  SidebarMobile, Sidebar, ButtonModule],
  templateUrl: './layout-component.html',
  styleUrl: './layout-component.scss',
})
export class LayoutComponent {
private breakpointObserver = inject(BreakpointObserver);

  // Creamos una señal reactiva que es true si la pantalla es > 768px (md de Tailwind)
  // 'min-width: 768px' coincide con el breakpoint 'md' de Tailwind
  isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 968px)')
      .pipe(map(result => result.matches)),
    { initialValue: false } // Valor inicial para evitar errores en SSR
  );
  // Drawer para mobile
  readonly drawerOpen = signal(false);

  openDrawer() { this.drawerOpen.set(true); }
  closeDrawer() { this.drawerOpen.set(false); }
}
