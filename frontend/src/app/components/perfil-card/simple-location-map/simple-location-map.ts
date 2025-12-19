import { Component, effect, ElementRef, input, ViewChild } from '@angular/core';
import * as L from 'leaflet';

type LatLng = { lat: number; lng: number };

@Component({
  selector: 'app-simple-location-map',
  standalone: true,
  imports: [],
  templateUrl: './simple-location-map.html',
  styleUrl: './simple-location-map.scss',
})
export class SimpleLocationMap {
  // Inputs
  ubicacion = input<LatLng | null>(null);
  mapHeight = input<string>('320px');
  zoom = input<number>(15); // Un zoom de 15 o 16 es ideal para ver calles
  textoLabel = input<string>('Ubicación de la persona');

  @ViewChild('mapContainer', { static: true })
  mapContainer!: ElementRef<HTMLDivElement>;

  private map?: L.Map;
  private marker?: L.Marker;

  // Pin verde simple
  private readonly greenIcon: L.DivIcon = L.divIcon({
    className: 'simple-green-pin',
    html: `
      <div class="pin">
        <div class="dot"></div>
      </div>
    `,
    iconSize: [28, 40],
    iconAnchor: [14, 40],
    popupAnchor: [0, -34],
  });

  constructor() {
    // ESTE EFECTO SE EJECUTA CUANDO CAMBIA LA UBICACIÓN (INPUT)
    effect(() => {
      const u = this.ubicacion();
      // Si el mapa ya existe, actualizamos.
      // Si no existe (carga inicial), lo ignoramos porque ngAfterViewInit se encargará.
      if (this.map && u) {
        this.updateMapState(u);
      } else if (this.map && !u) {
        this.clearMarker();
      }
    });
  }

  ngAfterViewInit(): void {
    this.initMap();

    // ✅ FIX: Una vez creado el mapa, forzamos la actualización visual
    // con la ubicación que tengamos en ese momento.
    const u = this.ubicacion();
    if (u) {
      this.updateMapState(u);
    }

    // Fix tamaño del mapa
    queueMicrotask(() => this.map?.invalidateSize());
  }

  // --- LÓGICA CENTRALIZADA ---
  private updateMapState(u: LatLng): void {
    if (!this.map) return;

    // 1. Mover o crear marcador
    this.setMarker(u);

    // 2. ✅ CENTRAR EL MAPA (Esto faltaba en la carga inicial)
    // Usamos flyTo para una animación suave, o setView para instantáneo
    this.map.setView([u.lat, u.lng], this.zoom());
  }

  private initMap(): void {
    if (this.map) return;

    const fallbackCenter: L.LatLngExpression = [-34.9214, -57.9544];
    this.map = L.map(this.mapContainer.nativeElement, {
      center: fallbackCenter,
      zoom: 12,
      zoomControl: true,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 20,
    }).addTo(this.map);
  }

  private setMarker(u: LatLng): void {
    if (!this.map) return;

    if (!this.marker) {
      this.marker = L.marker([u.lat, u.lng], { icon: this.greenIcon }).addTo(this.map);
      this.marker.bindTooltip(this.textoLabel(), {
        direction: 'top',
        offset: [0, -35],
        opacity: 0.9
      });
    } else {
      this.marker.setLatLng([u.lat, u.lng]);
    }
  }

  private clearMarker(): void {
    if (this.map && this.marker) {
      this.map.removeLayer(this.marker);
      this.marker = undefined;
    }
  }

  ngOnDestroy(): void {
    if (!this.map) return;
    this.map.off();
    this.map.remove();
    this.map = undefined;
    this.marker = undefined;
  }
}
