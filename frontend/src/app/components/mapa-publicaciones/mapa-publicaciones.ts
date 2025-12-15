import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, input, ViewChild } from '@angular/core';

import * as L from 'leaflet';
import { Publicacion } from '../../interfaces/publicacion.interface';

delete (L.Icon.Default.prototype as any)._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: '/assets/leaflet/marker-icon-2x.png',
  iconUrl: '/assets/leaflet/marker-icon.png',
  shadowUrl: '/assets/leaflet/marker-shadow.png',
});

@Component({
  selector: 'app-mapa-publicaciones',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mapa-publicaciones.html',
  styleUrl: './mapa-publicaciones.scss',
})
export class MapaPublicaciones implements AfterViewInit {
  publicaciones = input<Publicacion[]>([]);

  @ViewChild('mapContainer') mapContainer!: ElementRef<HTMLDivElement>;

  private map?: L.Map;
  private markersLayer = new L.LayerGroup();

  private readonly CENTRO_AR: L.LatLngExpression = [-34.9214, -64.9544];

  ngAfterViewInit(): void {
    this.ensureMap();
    this.renderMarkers(this.publicaciones());
  }

  public updateMarkers(publicaciones: Publicacion[]): void {
    this.renderMarkers(publicaciones);
  }

  private ensureMap(): void {
    if (this.map) {
      this.map.invalidateSize();
      return;
    }

    const el = this.mapContainer.nativeElement;

    this.map = L.map(el, {
      center: this.CENTRO_AR,
      zoom: 5,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
    }).addTo(this.map);

    this.map.addLayer(this.markersLayer);
  }

  private renderMarkers(publicaciones: Publicacion[]): void {
    if (!this.map) return;

    this.markersLayer.clearLayers();

    const markers: L.Marker[] = [];

    for (const pub of publicaciones) {
      const lat = pub.ubicacion.latitud;
      const lng = pub.ubicacion.longitud;

      if (lat && lng) {
        const marker = L.marker([lat, lng]);

        const popupContent = this.buildPopupContent(pub);

        marker.bindPopup(popupContent, {
          maxWidth: 250,
          minWidth: 150,
        });

        this.markersLayer.addLayer(marker);
        markers.push(marker);
      }
    }

    if (markers.length > 0) {
      const group = new L.FeatureGroup(markers);
      if (markers.length > 1) {
        this.map.fitBounds(group.getBounds(), { padding: [20, 20] });
      } else {
        this.map.setView(group.getBounds().getCenter(), 14);
      }
    }
  }

  private buildPopupContent(pub: Publicacion): string {
    let imageUrl = '';

    if (pub.fotos && pub.fotos.length > 0) {
      const firstPhotoUrl = pub.fotos[0].url;
      imageUrl = `
            <img
                src="${firstPhotoUrl}"
                alt="${pub.nombre}"
                style="max-width: 100%; height: auto; display: block; margin-bottom: 8px; border-radius: 4px;"
            />
        `;
    }

    const descriptionSnippet = pub.descripcion.substring(0, 50) + '...';

    // 🚩 Obtenemos Latitud y Longitud
    const lat = pub.ubicacion.latitud.toFixed(4); // Limitar a 4 decimales para limpieza
    const lng = pub.ubicacion.longitud.toFixed(4);

    return `
        <div style="font-family: Arial, sans-serif;">
            ${imageUrl}
            <h4 style="margin: 0 0 5px 0;">${pub.nombre}</h4>
            <p style="margin: 0 0 5px 0; font-size: 0.9em;">${descriptionSnippet}</p>

            <p style="margin: 0 0 5px 0; font-size: 0.8em; color: #444;">
                ${lat}, ${lng}
            </p>

            <p style="margin: 0 0 5px 0; font-size: 0.8em; color: #666;">
                Publicado: ${new Date(pub.fecha).toLocaleDateString()}
            </p>
            <a href="/app/publicaciones/detalle/${
              pub.id
            }" target="_blank" style="font-size: 0.9em; color: #007bff; text-decoration: none;">
                Ver detalle completo
            </a>
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
