import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withFetch,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { providePrimeNG } from 'primeng/config';

import Aura from '@primeuix/themes/aura';
//import { provideAnimations } from '@angular/platform-browser/animations';
import { provideAnimations } from '@angular/platform-browser/animations'; // 1. Importa esto
import { provideToastr } from 'ngx-toastr';
import { JwtInterceptor } from './Interceptors/JwtInterceptor.Interceptor';

import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';

registerLocaleData(localeEs, 'es-ES');

// Define el diccionario en español
const primeNgEs = {
    //firstDayOfWeek: 1,
    dayNames: ["domingo", "lunes", "martes", "miércoles", "jueves", "viernes", "sábado"],
    dayNamesShort: ["dom", "lun", "mar", "mié", "jue", "vie", "sáb"],
    dayNamesMin: ["Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa"],
    monthNames: ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"],
    monthNamesShort: ["ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"],
    today: 'Hoy',
    clear: 'Borrar',
    dateFormat: 'dd/mm/yy',
    weekHeader: 'Sem'
};

export const appConfig: ApplicationConfig = {
  providers: [
    {
      provide: LOCALE_ID,
      useValue: 'es-ES',
    },
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
    provideHttpClient(withFetch(), withInterceptorsFromDi()),
    // 👉 registrar tu interceptor como multi provider
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes ,withComponentInputBinding()),
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          ripple: true,
          darkModeSelector: 'none', // o false
          cssLayer: {
            name: 'primeng', // Debe coincidir con el nombre de la capa en styles.css
            order: 'tailwindcss, tailwindcss-primeui', // Debe coincidir con el orden en styles.css
          },
        },
      },
      translation: primeNgEs
    }),
    {
      provide: LOCALE_ID,
      useValue: 'es-ES',
    },
  ],
};
