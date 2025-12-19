import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  input,
  ViewChild,
  OnDestroy,
  effect,
  inject,
} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet.markercluster';
import { Publicacion } from '../../interfaces/publicacion.interface';
import { Avistamiento } from '../../interfaces/avistamiento.interface';
import { Ubicacion } from '../../interfaces/ubicacion.interface';
import { AuthStoreService } from '../../store/auth.stored.service';

type MarkerType = 'avistamiento' | 'publicacion';

const createMarkerIcon = (type: MarkerType = 'publicacion'): L.DivIcon => {
  const size = 30;
  return L.divIcon({
    html: `<div>${type == 'avistamiento' ? '!' : '?'}</div>`,
    className: 'custom-marker-icon ' + type,
    iconSize: [size, size],
    iconAnchor: [size / 2, size],
    popupAnchor: [0, -size],
  });
};

const createClusterIcon = (cluster: any, type: MarkerType = 'publicacion'): L.DivIcon => {
  const count = cluster.getChildCount();
  let size = 50;

  return L.divIcon({
    html: `<div><span>${count}</span></div>`,
    className: 'custom-cluster-icon ' + type,
    iconSize: [size, size],
    iconAnchor: [size / 2, size],
  });
};

// =========================================================================

@Component({
  selector: 'app-mapa-publicaciones',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mapa-publicaciones.html',
  styleUrl: './mapa-publicaciones.scss',
}) // =========================================================================
export class MapaPublicaciones implements AfterViewInit, OnDestroy {
  publicaciones = input<Publicacion[]>([]);
  avistamientos = input<Avistamiento[]>([]);
  ubicacionInicial = input<Ubicacion | null>(null);

  readonly authStore = inject(AuthStoreService);



  @ViewChild('mapContainer') mapContainer!: ElementRef<HTMLDivElement>;

  private map?: L.Map;
  // 💡 Mantenemos la capa general (LayerGroup) para agrupar las dos capas de clusters
  private allMarkersLayer: L.FeatureGroup = new L.FeatureGroup();

  // 💡 NUEVAS CAPAS DE CLUSTER (Una por tipo)
  private publicacionesLayer: any;
  private avistamientosLayer: any;

  private readonly publicacionIconInstance = createMarkerIcon('publicacion');
  private readonly avistamientoIconInstance = createMarkerIcon('avistamiento'); // Nueva instancia

  private readonly CENTRO_AR: L.LatLngExpression =  [-36.6773920760823,-60.5584771084959];

  constructor() {
    effect(() => {
      const pubs = this.publicaciones();
      const avists = this.avistamientos();

      if (this.map) {
        console.log('se llama', pubs)
        this.updateMarkers(pubs, avists);
      }
    });
  }

  ngAfterViewInit(): void {
    this.ensureMap();
  }

  public updateMarkers(publicaciones: Publicacion[], avistamientos: Avistamiento[]): void {
    console.log('update', this.avistamientos());

    this.renderMarkers(publicaciones, avistamientos);
  }

  private ensureMap(): void {
    if (this.map) {
      this.map.invalidateSize();
      return;
    }
    let centro = this.CENTRO_AR;

    const inicial = this.ubicacionInicial();

    if (inicial) {
      centro = [-36.6773920760823,-60.5584771084959];
    }
    const el = this.mapContainer.nativeElement;
    this.map = L.map(el, { center: centro, zoom: 15 });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 }).addTo(
      this.map
    );

    this.publicacionesLayer = (L as any).markerClusterGroup({
      maxClusterRadius: 30,
      iconCreateFunction: (c: any) => createClusterIcon(c, 'publicacion'),
    });

    this.avistamientosLayer = (L as any).markerClusterGroup({
      maxClusterRadius: 30,
      iconCreateFunction: (c: any) => createClusterIcon(c, 'avistamiento'),
    });

    this.allMarkersLayer.addLayer(this.avistamientosLayer);
    this.allMarkersLayer.addLayer(this.publicacionesLayer);

    this.map.addLayer(this.allMarkersLayer);

    this.updateMarkers(this.publicaciones(), this.avistamientos());
  }

  private fitMapToMarkers(): void {
    if (!this.map) return;

    // 1. Obtener los límites del FeatureGroup (que ahora tiene getBounds garantizado)
    const combinedBounds = this.allMarkersLayer.getBounds();

    if (combinedBounds.isValid()) {
      // Si hay al menos un marcador y los límites son válidos, centrar la vista
      this.map.fitBounds(combinedBounds, { padding: [50, 50] });
    } else {
      // 2. Si no hay marcadores o los límites no son válidos, intentar el caso de un único punto
      const allMarkers = [...this.publicaciones(), ...this.avistamientos()];

      if (allMarkers.length === 1) {
        // Acceder a la ubicación del único marcador
        const marker = allMarkers[0];
        const lat = marker.ubicacion.latitud;
        const lng = marker.ubicacion.longitud;

        // Centrar con un zoom fijo para un solo punto
        this.map.setView([lat, lng], 14);
      } else {
        // Cero marcadores, centrar en Argentina
        this.map.setView(this.CENTRO_AR, 5);
      }
    }
  }

  private renderMarkers(publicaciones: Publicacion[], avistamientos: Avistamiento[]): void {
    if (!this.map || !this.publicacionesLayer || !this.avistamientosLayer) return;

    // 1. Limpiamos ambas capas individuales
    this.avistamientosLayer.clearLayers();
    this.publicacionesLayer.clearLayers();

    // 2. Renderizar Avistamientos
    for (const avis of avistamientos) {
      const lat = avis.ubicacion.latitud;
      const lng = avis.ubicacion.longitud;

      if (lat && lng) {
        const marker = L.marker([lat, lng], { icon: this.avistamientoIconInstance });
        marker.bindPopup(this.buildPopupContent(avis, 'avistamiento'), {
          maxWidth: 250,
          minWidth: 150,
        });
        this.avistamientosLayer.addLayer(marker); // Añadir a su capa
      }
    }

    // 3. Renderizar Publicaciones
    for (const pub of publicaciones) {
      const lat = pub.ubicacion.latitud;
      const lng = pub.ubicacion.longitud;

      if (lat && lng) {
        const marker = L.marker([lat, lng], { icon: this.publicacionIconInstance });
        marker.bindPopup(this.buildPopupContent(pub, 'publicacion'), {
          maxWidth: 250,
          minWidth: 150,
        });
        this.publicacionesLayer.addLayer(marker); // Añadir a su capa
      }
    }

      // ✅ clave: dejar que el DOM/layout se estabilice y recién ahí recalcular size + fit
    requestAnimationFrame(() => {
      requestAnimationFrame(() => {
        this.map?.invalidateSize(true);
        this.fitMapToMarkers();
      });
    });
  }

  get urlDetallePublicacion(): string {
    if (this.authStore.isAuthenticated() && this.authStore.isAdmin()) {
      return '/admin/publicaciones/detalle/';
    }
    if(this.authStore.isAuthenticated() && !this.authStore.isAdmin()){
      return '/app/publicaciones/detalle/';
    }
    return '/public/login';
  }

  get avistamiento(): string {
    if (this.authStore.isAuthenticated() && this.authStore.isAdmin()) {
      return '/admin/avistamientos/detalle/';
    }
    if(this.authStore.isAuthenticated() && !this.authStore.isAdmin()){
      return '/app/avistamientos/detalle/';
    }
    return '/public/login';
  }

  // 💡 buildPopupContent ahora recibe el tipo
  private buildPopupContent(
    item: Publicacion | Avistamiento,
    type: 'publicacion' | 'avistamiento'
  ): string {
    const isPublicacion = type === 'publicacion';

    const title = isPublicacion ? (item as Publicacion).nombre : 'Avistamiento';
    const description = item.descripcion.substring(0, 50) + '...';
    const detailUrl = isPublicacion
      ? `${this.urlDetallePublicacion}${item.id}`
      : `${this.avistamiento}${item.id}`; // Asume una ruta de avistamiento



    let imageUrl = '';
    const photos = (item as any).fotos;
    if (photos && photos.length > 0) {
      const firstPhotoUrl = photos[0].url;
      imageUrl = `
            <img
                src="${firstPhotoUrl}"
                alt="${title}"
                style="max-width: 100%; height: auto; display: block; margin-bottom: 8px; border-radius: 4px;"
            />
        `;
    }

    const lat = item.ubicacion.latitud.toFixed(4);
    const lng = item.ubicacion.longitud.toFixed(4);

    return `
        <div style="font-family: Arial, sans-serif;">
            ${imageUrl}
            <h4 style="margin: 0 0 5px 0;">${title}</h4>
            <p style="margin: 0 0 5px 0; font-size: 0.9em;">${description}</p>
            <p style="margin: 0 0 5px 0; font-size: 0.8em; color: #666;">
                ${lat}, ${lng}
            </p>
            ${isPublicacion ? `<a href="${detailUrl}" target="_blank" style="font-size: 0.9em; color: #007bff; text-decoration: none;">
                Ver detalle completo
            </a>`: ''}

        </div>
    `;
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.off();
      this.map.remove();
      this.map = undefined;
    }
  }
}
