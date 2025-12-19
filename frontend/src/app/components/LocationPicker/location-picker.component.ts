import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  computed,
  effect,
  ElementRef,
  inject,
  input,
  output,
  Output,
  signal,
  ViewChild,
} from '@angular/core';
import { Dialog } from 'primeng/dialog';
import * as L from 'leaflet'; // Asegúrate de tener esto
import { ButtonModule } from 'primeng/button';
import { GeorefApiExternaService } from '../../services/georefApiExterna.service';
import { UbicacionExternaResponse } from '../../interfaces/ubicacionExternaResponse';
import { FormsModule } from '@angular/forms';
import { UbicacionCreate } from '../../interfaces/ubicacion.interface';
import { Tooltip } from 'primeng/tooltip';

export type LatLng = { lat: number; lng: number };

@Component({
  selector: 'app-location-picker',
  standalone: true,
  imports: [CommonModule, Dialog, ButtonModule, FormsModule, Tooltip],
  templateUrl: './location-picker.component.html',
  styleUrls: ['./location-picker.component.scss'],
})
export class LocationPickerComponent implements AfterViewInit {
  private georef = inject(GeorefApiExternaService);

  // ... (tus inputs, signals y variables existentes siguen igual) ...
  public vistaOpciones = signal<'lista' | 'mapa'>('mapa');
  isInvalid = input<boolean>(false);
  initialLocation = input<UbicacionCreate | null>(null);
  onLocationSelected = output<UbicacionCreate>();
  dialogOpen = signal(false);
  confirmed = signal<UbicacionCreate | null>(null);
  temp = signal<UbicacionCreate | null>(null);
  loadingGeoref = signal(false);
  georefText = signal('');
  @ViewChild('mapContainer') mapContainer!: ElementRef<HTMLDivElement>;

  private map?: L.Map;
  private tempMarker?: L.Marker;
  private confirmedMarker?: L.Marker;
  private readonly LA_PLATA: L.LatLngExpression = [-34.9214, -57.9544];
  loadingMap = signal(true);

  // ✅ 1. DEFINIMOS EL PUNTITO VERDE AQUÍ
  // Usamos divIcon para dibujar un círculo con CSS puro
private greenIcon = L.divIcon({
    className: 'custom-css-icon',
    html: `
      <div style="
        background-color: #1644b7ff;
        width: 30px;
        height: 30px;
        border-radius: 50% 50% 50% 0; /* Aquí está la magia: 3 lados redondos, 1 en punta */
        transform: rotate(-45deg);   /* Rotamos para que la punta mire hacia abajo */
        border: 2px solid white;
        box-shadow: 1px 1px 4px rgba(0,0,0,0.5);
        display: flex;
        align-items: center;
        justify-content: center;
      ">
        <div style="
          width: 8px;
          height: 8px;
          background-color: white;
          border-radius: 50%;
        "></div>
      </div>
    `,
    // El icono visualmente es mas grande que 30px por la rotacion (diagonal)
    // La diagonal de 30px es aprox 42px.
    iconSize: [30, 42],
    iconAnchor: [15, 42], // La punta del pin está abajo al centro
    popupAnchor: [0, -36]
  });
  isValidUbicacion = computed(() => {
    const c = this.temp();
    return !!(c && !c.idExternoProvincia && !c.provincia);
  });

  textoLabel = computed(() => {
    const georef = this.georefText()?.trim();
    if (georef) return georef;
    const loc = this.initialLocation();
    if (loc?.provincia) {
      return `${this.initialLocation()?.provincia}, ${
        this.initialLocation()?.departamento
      }  ${
        this.initialLocation()?.municipio
          ? ' ,' + this.initialLocation()?.municipio
          : ''
      } `;
    }
    return 'Sin ubicación';
  });

  constructor() {
    effect(() => {
      const isHasConfirmado = this.confirmed();
      if (!isHasConfirmado && this.dialogOpen()) {
        this.confirmed.set(this.initialLocation());
      } else if (this.initialLocation()) {
      }

      if (!this.dialogOpen()) {
        this.renderConfirmedMarker();
      }
    });

    effect(() => {
      if (this.dialogOpen()) {
        this.temp.set(this.confirmed());
        if (this.map) {
          this.map.invalidateSize();
        }

        queueMicrotask(() => {
          this.ensureMap();
          this.hideConfirmedMarker();
          this.renderTempFromState();
        });
      } else {
        this.clearTempMarker();
        this.renderConfirmedMarker();
      }
    });
  }
  ngAfterViewInit(): void {}

  // ... (Tus métodos open, initMap, cancel, confirmMap siguen igual) ...

  open(): void {
    this.dialogOpen.set(true);
    queueMicrotask(() => {
      setTimeout(() => {
        this.initMap();
      }, 50);
    });
  }

  private initMap() {
    if (this.map) {
      this.map.invalidateSize();
      this.loadingMap.set(false);
      return;
    }

    this.map = L.map(this.mapContainer.nativeElement, {
      center: [-34.9214, -57.9544],
      zoom: 13,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
    }).addTo(this.map);

    setTimeout(() => {
      this.map?.invalidateSize();
      this.loadingMap.set(false);
    }, 100);
  }

  cancel(): void {
    if (!this.confirmed()) {
      this.temp.set(null);
      this.georefText.set('');
    }
    this.dialogOpen.set(false);
    this.destroyMap();
  }

  confirmMap(event: Event): void {
    event.preventDefault();
    this.confirmed.set(this.temp());
    this.onLocationSelected.emit(this.confirmed()!);
    this.dialogOpen.set(false);
    this.destroyMap();
  }

  onConfirmToSelectedList(ubicacionSelected: UbicacionCreate) {
    this.confirmed.set(ubicacionSelected);
    this.onLocationSelected.emit(ubicacionSelected!);
    this.dialogOpen.set(false);
    this.destroyMap();
  }

  // ---------------- MAP ----------------

  private ensureMap(): void {
    if (this.map) {
      this.map.invalidateSize();
      this.centerMap();
      return;
    }

    const el = this.mapContainer?.nativeElement;
    if (!el) return;

    this.map = L.map(el).setView(this.LA_PLATA, 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
    }).addTo(this.map);

// 👇 AQUÍ ESTÁ EL CAMBIO
    this.map.on('click', (e: L.LeafletMouseEvent) => {
      // 🛑 1. Detenemos la propagación del evento nativo
      // Esto evita que el click "salga" del mapa hacia el componente padre o el dialog
      L.DomEvent.stopPropagation(e.originalEvent);

      // Opcional: previene comportamientos por defecto del navegador si fuera necesario
      // e.originalEvent.preventDefault();

      const { lat, lng } = e.latlng;

      const temp: UbicacionCreate = {
        latitud: lat,
        longitud: lng,
      };

      this.temp.set(temp);
      this.renderTempMarker(lat, lng);
      this.fetchGeoref(temp);
    });

    this.centerMap();
  }

  private centerMap(): void {
    const t = this.temp() ?? this.confirmed();
    if (!this.map) return;

    if (t?.latitud && t?.longitud) {
      this.map.setView([+t.latitud, +t.longitud], 14);
    } else {
      this.map.setView(this.LA_PLATA, 12);
    }
  }

  // ---------------- MARKERS (MODIFICADOS) ----------------

  private renderTempFromState(): void {
    const t = this.temp();
    if (t?.latitud && t?.longitud) {
      this.renderTempMarker(+t.latitud, +t.longitud);
    } else {
      this.clearTempMarker();
    }
  }

  private renderTempMarker(lat: number, lng: number): void {
    if (!this.map) return;

    if (!this.tempMarker) {
      // ✅ AQUI AGREGAMOS { icon: this.greenIcon }
      this.tempMarker = L.marker([lat, lng], { icon: this.greenIcon }).addTo(
        this.map
      );
    } else {
      this.tempMarker.setLatLng([lat, lng]);
    }
  }

  private clearTempMarker(): void {
    if (this.map && this.tempMarker) {
      this.map.removeLayer(this.tempMarker);
      this.tempMarker = undefined;
    }
  }

  private hideConfirmedMarker(): void {
    if (this.map && this.confirmedMarker) {
      this.map.removeLayer(this.confirmedMarker);
      this.confirmedMarker = undefined;
    }
  }

  private renderConfirmedMarker(): void {
    if (!this.map) return;
    this.hideConfirmedMarker();
    const c = this.confirmed();
    if (!c?.latitud || !c?.longitud) return;

    // ✅ AQUI TAMBIEN AGREGAMOS { icon: this.greenIcon }
    this.confirmedMarker = L.marker([+c.latitud, +c.longitud], {
      icon: this.greenIcon,
    }).addTo(this.map);
  }

  // ... (El resto de tus métodos fetchGeoref y destroyMap siguen igual) ...
  private fetchGeoref(temp: UbicacionCreate): void {
    this.loadingGeoref.set(true);
    this.georefText.set('');

    this.georef
      .getUbicacionExterna(temp.latitud.toString(), temp.longitud.toString())
      .subscribe({
        next: (res: UbicacionExternaResponse) => {
          const u = res.ubicacion;

          const enriched: UbicacionCreate = {
            provincia: u?.provincia?.nombre,
            idExternoProvincia: u?.provincia?.id,
            municipio: u?.municipio?.nombre,
            idExternoMunicipio: u?.municipio?.id,

            departamento: u?.departamento?.nombre,
            idExternoDepartamento: u?.departamento?.id,
            latitud: u?.lat!,
            longitud: u?.lon!,
          };

          console.log('Ubicación enriquecida:', enriched);

          this.temp.set(enriched);

          this.georefText.set(
            `${enriched?.provincia}, ${enriched?.departamento}  ${
              enriched?.municipio ? ',' + enriched.municipio : ''
            } `
          );

          this.loadingGeoref.set(false);
        },
        error: () => {
          this.loadingGeoref.set(false);
          this.georefText.set('No se pudo obtener la ubicación');
        },
      });
  }

  private destroyMap(): void {
    if (!this.map) return;
    this.map.off();
    this.map.remove();
    this.map = undefined;
    this.tempMarker = undefined;
    this.confirmedMarker = undefined;
  }
}
