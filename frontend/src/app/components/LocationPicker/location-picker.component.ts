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

import * as L from 'leaflet';
import { ButtonModule } from 'primeng/button';
import { GeorefApiExternaService } from '../../services/georefApiExterna.service';
import { UbicacionExternaResponse } from '../../interfaces/ubicacionExternaResponse';
import { FormsModule } from '@angular/forms';
import { UbicacionCreate } from '../../interfaces/ubicacion.interface';

export type LatLng = { lat: number; lng: number };



@Component({
  selector: 'app-location-picker',
  standalone: true,
  imports: [CommonModule, Dialog, ButtonModule,  FormsModule, ],
  templateUrl: './location-picker.component.html',
  styleUrls: ['./location-picker.component.scss'],
})
export class LocationPickerComponent implements AfterViewInit {

  private georef = inject(GeorefApiExternaService);


    // Tu signal inicializada
    public vistaOpciones = signal<'lista' | 'mapa'>('mapa');


  // INPUT / OUTPUT
  isInvalid = input<boolean>(false)
  initialLocation = input<UbicacionCreate | null>(null);
  onLocationSelected = output<UbicacionCreate>();

  dialogOpen = signal(false);

  // estado
  confirmed = signal<UbicacionCreate | null>(null);
  temp = signal<UbicacionCreate | null>(null);

  loadingGeoref = signal(false);
  georefText = signal('');

  @ViewChild('mapContainer') mapContainer?: ElementRef<HTMLDivElement>;

  private map?: L.Map;
  private tempMarker?: L.Marker;
  private confirmedMarker?: L.Marker;

  private readonly LA_PLATA: L.LatLngExpression = [-34.9214, -57.9544];



  textoLabel = computed(() => {
    const georef = this.georefText()?.trim();
    if (georef) return georef;

    const loc = this.initialLocation();
    if (loc?.provincia) {
      return `${this.initialLocation()?.provincia}, ${this.initialLocation()?.departamento}  ${this.initialLocation()?.municipio ?  ' ,'+this.initialLocation()?.municipio : ''} `;
    }

    return 'Sin ubicación';
  });


  constructor() {
    // sincroniza input del padre
    effect(() => {

      const isHasConfirmado= this.confirmed();
      if(!isHasConfirmado){

          this.confirmed.set(this.initialLocation());
      }else if(this.initialLocation()){

      }

      if (!this.dialogOpen()) {
        this.renderConfirmedMarker();
      }
    });

    // apertura / cierre modal
    effect(() => {
      if (this.dialogOpen()) {
        this.temp.set(this.confirmed());
        if(this.map){
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
  ngAfterViewInit(): void {

  }

  // ---------------- UI ----------------

  open(): void {
    this.dialogOpen.set(true);
    if(this.map){
      this.map.invalidateSize();
    }


  }

  cancel(): void {

    if(!this.confirmed()){
    this.temp.set(null);
    this.georefText.set('')
    }

    this.dialogOpen.set(false);
    this.destroyMap();
  }

  confirmMap(event: Event): void {
    event.preventDefault();
    //
    this.confirmed.set(this.temp());
    this.onLocationSelected.emit(this.confirmed()!);
    this.dialogOpen.set(false);

    this.destroyMap()
  }

  onConfirmToSelectedList(ubicacionSelected:UbicacionCreate ){
    this.confirmed.set(ubicacionSelected);
    this.onLocationSelected.emit(ubicacionSelected!);
    this.dialogOpen.set(false);
    this.destroyMap()
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

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      const { lat, lng } = e.latlng;


      const temp: UbicacionCreate = {
        latitud: (lat),
        longitud: (lng),
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

  // ---------------- MARKERS ----------------

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
      this.tempMarker = L.marker([lat, lng]).addTo(this.map);
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

    this.confirmedMarker = L.marker([+c.latitud, +c.longitud]).addTo(this.map);
  }

  // ---------------- GEOREF ----------------

  private fetchGeoref(temp: UbicacionCreate): void {
    this.loadingGeoref.set(true);
    this.georefText.set('');

    this.georef.getUbicacionExterna(temp.latitud.toString(), temp.longitud.toString()).subscribe({
      next: (res: UbicacionExternaResponse) => {
        const u = res.ubicacion;

        const enriched: UbicacionCreate = {
          provincia: u?.provincia?.nombre,
          idExternoProvincia: u?.provincia?.id,
          municipio: u?.municipio?.nombre,
          idExternoMunicipio: u?.municipio?.id,

          departamento: u?.departamento?.nombre,
          idExternoDepartamento: u?.departamento?.id,
          latitud:u?.lat!,
          longitud:u?.lon!
        };


        this.temp.set(enriched);

        this.georefText.set(
          `${enriched?.provincia}, ${enriched?.departamento}  ${(enriched?.municipio ? (','+enriched.municipio) : '')} `
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

  // 🔥 elimina listeners + DOM interno de leaflet
  this.map.off();
  this.map.remove();

  // limpiamos referencias
  this.map = undefined;
  this.tempMarker = undefined;
  this.confirmedMarker = undefined;


}







}
