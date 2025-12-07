import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { providePrimeNG } from 'primeng/config';

import Aura from '@primeuix/themes/aura';
//import { provideAnimations } from '@angular/platform-browser/animations';
import { provideAnimations } from '@angular/platform-browser/animations'; // 1. Importa esto
import { provideToastr } from 'ngx-toastr';
import { JwtInterceptor } from './Interceptors/JwtInterceptor.Interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimations(), // required animations providers
    provideToastr({
      // 2. Provee la configuración global (Opcional)
      timeOut: 3000,
      positionClass: 'toast-top-right',
      //preventDuplicates: true,
      progressBar: true,
      closeButton: true,
    }),

    // 👉 importante: usar withInterceptorsFromDi
    provideHttpClient(
      withFetch(),
      withInterceptorsFromDi()),
    // 👉 registrar tu interceptor como multi provider
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    providePrimeNG({
      theme: {
                preset: Aura,
                options:{
                    ripple: true
                    ,darkModeSelector: 'none', // o false
                  cssLayer: {
                              name: 'primeng', // Debe coincidir con el nombre de la capa en styles.css
                              order: 'tailwindcss, tailwindcss-primeui' // Debe coincidir con el orden en styles.css
                            }
                }
            }
    })
  ]
};
