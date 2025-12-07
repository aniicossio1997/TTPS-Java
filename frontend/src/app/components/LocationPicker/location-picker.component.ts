import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  inject,
  input,
  Output,
  signal,
  ViewChild,

} from '@angular/core';
import { Dialog } from 'primeng/dialog';

import * as L from 'leaflet';
import { ButtonModule } from 'primeng/button';
import { GeorefApiExternaService } from '../../services/georefApiExterna.service';
import { UbicacionExternaResponse } from '../../interfaces/ubicacionExternaResponse';

@Component({
  selector: 'app-location-picker',
  standalone: true,
  imports: [CommonModule, Dialog, ButtonModule],
  templateUrl: './location-picker.component.html',
  styleUrls: ['./location-picker.component.scss'],
})
export class LocationPickerComponent implements AfterViewInit {

  titleButton =input<string>('Seleccionar ubicación');
  isInvalid =input<boolean>(false);
  @ViewChild('map') mapContainer!: ElementRef<HTMLDivElement>;
  @Output() locationSelected = new EventEmitter<{ lat: number; lng: number ,ubicacionExterna: UbicacionExternaResponse}>();

  ubicacionObtenida =signal<UbicacionExternaResponse|null>(null);


  visibleMapa: boolean = false;
  private map!: L.Map;
  private marker?: L.Marker;

  // Centro por defecto: La Plata
  private readonly defaultCenter: L.LatLngExpression = [-34.92194024693632, -57.959831717733195];
  private readonly defaultZoom = 12;

  selectedLat?: number;
  selectedLng?: number;

  ngAfterViewInit(): void {
    // 👉 Ya no pedimos geolocalización acá, solo centramos en La Plata
    //this.createMap(this.defaultCenter);
  }

  // Crear el mapa
  private createMap(center: L.LatLngExpression): void {
    this.map = L.map(this.mapContainer.nativeElement).setView(center, this.defaultZoom);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    // Click del usuario en el mapa
    this.map.on('click', (event: L.LeafletMouseEvent) => {
      const { lat, lng } = event.latlng;
      this.setMarker([lat, lng]);
      this.selectedLat = lat;
      this.selectedLng = lng;
      this.obtenerUbicacionExterna(lat.toString(), lng.toString());
      //this.locationSelected.emit({ lat, lng });

    });
  }

  private setMarker(latlng: L.LatLngExpression): void {
    if (this.marker) {
      this.marker.removeFrom(this.map);
    }
    this.marker = L.marker(latlng).addTo(this.map);
  }

  /** 👉 Llamá esto cuando se abre el modal (onShow del p-dialog) */
  refreshMap() {
    if (this.map) {
      setTimeout(() => {
        this.map.invalidateSize();
      }, 0);
    }
  }

  showDialogMapa() {
    this.visibleMapa = true;
          // Esperamos a que el modal esté visible y luego refrescamos el mapa
    setTimeout(() => {
          if (!this.map) {
            this.createMap(this.defaultCenter);
          }else{
            this.map.invalidateSize()
          }

              this.refreshMap();
          }, 50);
  }

  /** 👉 Botón opcional: intentar centrar en la ubicación del usuario */
  centerOnUserLocation() {
    if (!this.map) return;
    if (!('geolocation' in navigator)) return;

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const center: [number, number] = [
          position.coords.latitude,
          position.coords.longitude,
        ];

        this.map.setView(center, this.defaultZoom);
        this.setMarker(center);

        this.selectedLat = center[0];
        this.selectedLng = center[1];
        //this.locationSelected.emit({ lat: center[0], lng: center[1] });
      },
      (error) => {
        console.warn('No se pudo obtener la ubicación del usuario', error);
        // Si falla, simplemente nos quedamos donde estamos (La Plata o lo que haya)
      },
      {
        enableHighAccuracy: true,
        timeout: 5000,
      }
    );
  }


  private georefApiExternaService = inject(GeorefApiExternaService);



  private obtenerUbicacionExterna(lat: string, lon: string) {
    this.georefApiExternaService.getUbicacionExterna(lat, lon).subscribe({
      next: (response) => {
        this.ubicacionObtenida.set(response);
        // Aquí podés hacer algo con la respuesta, como mostrarla en el mapa o en la UI
      },
      error: (error) => {
        console.error('Error al obtener la ubicación externa:', error);
      }
    });
  }

    // Botón "Confirmar": ahora sí avisamos al padre
  confirmSelection() {
    if (this.selectedLat === undefined || this.selectedLng === undefined) return;

    this.locationSelected.emit({
      lat: this.selectedLat,
      lng: this.selectedLng,
      ubicacionExterna: this.ubicacionObtenida()!
    });

    this.selectedLat = undefined;
    this.selectedLng = undefined;
    this.ubicacionObtenida.set(null);

    if (this.marker) {
      this.marker.removeFrom(this.map);
      this.marker = undefined;
    }


    this.visibleMapa = false;
  }

  // Qué hacer cuando se cierra el modal (Cancelar, X, o después de Confirmar)
  onDialogHide() {
    // opción A: limpiar selección interna (pero el padre ya tiene la que emitiste al confirmar)
    this.selectedLat = undefined;
    this.selectedLng = undefined;
    this.ubicacionObtenida.set(null);

    if (this.marker) {
      this.marker.removeFrom(this.map);
      this.marker = undefined;
    }

    this.visibleMapa = false;
    // Si quisieras dejar todo tal cual estaba para la próxima apertura, NO hagas esto.
  }


  // Si más adelante querés customizar el ícono:
  /*
  private customIcon = L.icon({
    iconUrl: 'assets/marker-icon.png',
    iconRetinaUrl: 'assets/marker-icon-2x.png',
    shadowUrl: 'assets/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });
  */



}
