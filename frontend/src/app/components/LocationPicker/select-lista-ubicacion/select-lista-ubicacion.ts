import { Component, computed, effect, inject, input, OnInit, output, signal } from '@angular/core';
import { SelectModule } from 'primeng/select';
import { GeorefService } from '../../../services/georef.service';
import { ProvinciasStoreService } from '../../../store/provincias.stored.service';
import { ProvinciaDTO } from '../../../interfaces/georef/provinciaDTO';
import { ApiStatus } from '../../../interfaces/local/EnumApiStatus.enum';
import { ToastrService } from 'ngx-toastr';
import { FormsModule } from '@angular/forms';
import { DepartamentoDTO } from '../../../interfaces/georef/departamentoDTO';
import { UbicacionSeleccionada } from '../location-picker.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-select-lista-ubicacion',
  imports: [SelectModule, FormsModule, CommonModule],
  templateUrl: './select-lista-ubicacion.html',
  styleUrl: './select-lista-ubicacion.scss',
  providers:[GeorefService, ProvinciasStoreService]
})
export class SelectListaUbicacion implements OnInit {

  private readonly toastr = inject(ToastrService);

  private readonly serviceGeoref = inject(GeorefService)
  readonly provinciasStore = inject(ProvinciasStoreService)
    // seleccionado (id)
// 1. Transformar propiedades a Señales
  provinciaSelected = signal<ProvinciaDTO | null>(null);
  departamentoSelected = signal<DepartamentoDTO | null>(null);

  departamentosOptions = signal<DepartamentoDTO[]>([]);


  ubicacionPrecargadaInput= input<UbicacionSeleccionada| null>(null)

  onUbicacionSelectedOutput= output<UbicacionSeleccionada>()

  ubiacionConfirmada = signal<UbicacionSeleccionada| null>(null)

  provinciasOptions= computed(()=> this.provinciasStore.provincias())
  isDepartamentoDisabled = computed(() => !this.provinciaSelected());

  private _status  = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError   = computed(() => this._status() === ApiStatus.ERROR);

  constructor(){
    effect(()=>{

      const isHasConfirmado= this.ubiacionConfirmada();
      if(!isHasConfirmado){
          this.ubiacionConfirmada.set(this.ubicacionPrecargadaInput());
          const ubicacionInicial=this.ubicacionPrecargadaInput()
          if(ubicacionInicial){

            this.onProvinciaChange({
              id:ubicacionInicial!.idExternoProvincia!,
              nombre: ubicacionInicial!.provincia!,
              centroide_lat:Number(ubicacionInicial!.lat!),
              centroide_lon: Number(ubicacionInicial!.lng!),
            })

            this.departamentoSelected.set({
              id:ubicacionInicial!.idExternoDepartamento!,
              nombre: ubicacionInicial!.departamento!,
              centroide_lat:Number(ubicacionInicial!.lat!),
              centroide_lon: Number(ubicacionInicial!.lng!),

            })

        }
    }


    })

    effect(()=>{
      const departamento = this.departamentoSelected()

      if(departamento && String(departamento?.centroide_lat) != this.ubicacionPrecargadaInput()?.lat &&  String(departamento.centroide_lon) !=this.ubicacionPrecargadaInput()?.lng){
        this.onUbicacionSelectedOutput.emit({
          lat: String(departamento.centroide_lat),
          lng: String(departamento.centroide_lon),
          idExternoProvincia: this.provinciaSelected()!.id,
          provincia: this.provinciaSelected()?.nombre!,
          idExternoDepartamento: departamento.id,
          departamento: departamento.nombre
        })
      }
    })
  }
  ngOnInit(): void {
    this.provinciasStore._getProvincias();


    /*

    06 { "id": "06", "nombre": "Buenos Aires", "centroide_lat": -34.474863669009004, "centroide_lon": -58.80432128906251 }

    */

  }


    // Función que se ejecuta cuando se selecciona una provincia
  onDepartamentoChange(departamento: DepartamentoDTO) {
    // Aquí puedes actualizar la señal de departamentoSelected
    this.departamentoSelected.set(departamento);  // Resetear departamento al cambiar la provincia
    this.ubiacionConfirmada.set({
          lat: String(departamento.centroide_lat),
          lng: String(departamento.centroide_lon),
          idExternoProvincia: this.provinciaSelected()!.id,
          provincia: this.provinciaSelected()?.nombre!,
          idExternoDepartamento: departamento.nombre,
          departamento: departamento.id
    })

    // Cargar departamentos aquí usando el ID de la provincia
  }

  onProvinciaChange(provincia: ProvinciaDTO | null) {


    // reset dependientes
    this.provinciaSelected.set(provincia);

    // Reseteamos el departamento seleccionado y sus opciones
    this.departamentoSelected.set(null);
    this.departamentosOptions.set([]);

    if(!provincia){
      return;
    }
    this._status.set(ApiStatus.LOADING);

    // OJO: tu service recibe number, pero Georef devuelve id string ("22", "06"...)
    // convertimos a number sin romper:
    const idProvinciaNumber = Number(this.provinciaSelected()!.id);

    this.serviceGeoref
      .getDepartamentos(idProvinciaNumber)
      .subscribe({
      next: (resp) => {
        this._status.set(ApiStatus.SUCCESS);
        this.departamentosOptions.set(resp.departamentos)

      },
      error: (err) => {
        console.error('Login error:', err);
        this._status.set(ApiStatus.ERROR);
        this.toastr.error('Error al recuperar los departamentos', 'Error en el servidor');
      }
    });
  }

  clearProvincia() {
    this.provinciaSelected.set(null);
    this.departamentoSelected.set(null);
    this.departamentosOptions.set([]);
  }


}
