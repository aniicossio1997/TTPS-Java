import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  computed,
  DestroyRef,
  effect,
  ElementRef,
  EventEmitter,
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
import { SelectButton } from 'primeng/selectbutton';
import { FormsModule } from '@angular/forms';
import { SelectListaUbicacion } from './select-lista-ubicacion/select-lista-ubicacion';

export type LatLng = { lat: number; lng: number };

// location.types.ts
export type UbicacionSeleccionada = {
  lat: string;   // 👈 strings para calzar con tus FormControl<string>
  lng: string;

  // GeoRef (opcionales)
  provincia?: string;
  idExternoProvincia?: string;

  municipio?: string;
  idExternoMunicipio?: string;

  departamento?: string;
  idExternoDepartamento?: string;
};



@Component({
  selector: 'app-location-picker',
  standalone: true,
  imports: [CommonModule, Dialog, ButtonModule, SelectButton, FormsModule,SelectListaUbicacion ],
  templateUrl: './location-picker.component.html',
  styleUrls: ['./location-picker.component.scss'],
})
export class LocationPickerComponent implements AfterViewInit {

  private georef = inject(GeorefApiExternaService);

  // Opciones estáticas (no necesitan ser signal si no cambian)
    public readonly stateOptions = [
      { icon: 'pi pi-list', label: 'Lista', value: 'lista' },
      { icon: 'pi pi-map-marker', label: 'Mapa', value: 'mapa' },
    ];

    // Tu signal inicializada
    public vistaOpciones = signal<'lista' | 'mapa'>('lista');


  // INPUT / OUTPUT
  isInvalid = input<boolean>(false)
  initialLocation = input<UbicacionSeleccionada | null>(null);
  onLocationSelected = output<UbicacionSeleccionada>();

  dialogOpen = signal(false);

  // estado
  confirmed = signal<UbicacionSeleccionada | null>(null);
  temp = signal<UbicacionSeleccionada | null>(null);

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

    return 'Click para seleccionar una ubicación';
  });


  constructor() {
    // sincroniza input del padre
    effect(() => {

      const isHasConfirmado= this.confirmed();
      if(!isHasConfirmado){
          this.confirmed.set(this.initialLocation());
      }

      if (!this.dialogOpen()) {
        this.renderConfirmedMarker();
      }
    });

    // apertura / cierre modal
    effect(() => {
      if (this.dialogOpen()) {
        this.temp.set(this.confirmed());

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
  }

  cancel(): void {

    this.temp.set(null);
    this.georefText.set('')
    this.dialogOpen.set(false);
     this.destroyMap();
  }

  confirmMap(): void {

    //
    this.confirmed.set(this.temp());
    this.onLocationSelected.emit(this.confirmed()!);
    this.dialogOpen.set(false);
  }

  onConfirmToSelectedList(ubicacionSelected:UbicacionSeleccionada ){
    this.confirmed.set(ubicacionSelected);
    this.onLocationSelected.emit(ubicacionSelected!);
    this.dialogOpen.set(false);
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

      console.log("MAPA::,",e)
      const temp: UbicacionSeleccionada = {
        lat: String(lat),
        lng: String(lng),
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

    if (t?.lat && t?.lng) {
      this.map.setView([+t.lat, +t.lng], 14);
    } else {
      this.map.setView(this.LA_PLATA, 12);
    }
  }

  // ---------------- MARKERS ----------------

  private renderTempFromState(): void {
    const t = this.temp();
    if (t?.lat && t?.lng) {
      this.renderTempMarker(+t.lat, +t.lng);
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
    if (!c?.lat || !c?.lng) return;

    this.confirmedMarker = L.marker([+c.lat, +c.lng]).addTo(this.map);
  }

  // ---------------- GEOREF ----------------

  private fetchGeoref(temp: UbicacionSeleccionada): void {
    this.loadingGeoref.set(true);
    this.georefText.set('');

    this.georef.getUbicacionExterna(temp.lat, temp.lng).subscribe({
      next: (res: UbicacionExternaResponse) => {
        const u = res.ubicacion;

        console.log("UBICACION SELECCIONADA fetchGeoref::", u)
        const enriched: UbicacionSeleccionada = {
          ...temp,
          provincia: u?.provincia?.nombre,
          idExternoProvincia: u?.provincia?.id,
          municipio: u?.municipio?.nombre,
          idExternoMunicipio: u?.municipio?.id,

          departamento: u?.departamento?.nombre,
          idExternoDepartamento: u?.departamento?.id,
        };

        console.log("MAPA PING::", enriched)

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
