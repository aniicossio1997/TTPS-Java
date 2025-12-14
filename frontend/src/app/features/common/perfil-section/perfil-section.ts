import { Component, inject, signal, computed, ChangeDetectionStrategy, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { PerfilCard } from '../../../components/perfil-card/perfil-card';
import { TabsModule } from 'primeng/tabs';
import { ButtonModule } from 'primeng/button';
import { EditarPerfil } from '../../../components/editar-perfil/editar-perfil';
import { SpeedDialModule } from 'primeng/speeddial';
import { EditPassword } from '../../../components/edit-password/edit-password';
import { AuthStoreService } from '../../../store/auth.stored.service';
import { PulicacionesUser } from './pulicaciones-user/pulicaciones-user';

enum TapEnum{
  TAP_0="0",
  TAP_1="1",
  TAP_2="2",
}

const TAPS_CONST={
  perfil:'perfil',
  publicaciones:'publicaciones',
  avistamientos:'avistamientos'
} as const;

enum OpcionesDeEdicionENUM{
  VER_PERFIL="VER_PERFIL",
  EDITAR_PERDIL="EDITAR_PERDIL",
  EDITAR_PASSWORD="EDITAR_PASSWORD",
}



@Component({
  selector: 'app-perfil-section',
  imports: [ TabsModule, PerfilCard, ButtonModule, EditarPerfil, EditPassword, PulicacionesUser],
  templateUrl: './perfil-section.html',
  styleUrl: './perfil-section.scss',
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  changeDetection: ChangeDetectionStrategy.OnPush,

})
export class PerfilSection {

  private authStore= inject(AuthStoreService)

  activeTab = signal(this.tapEnum.TAP_0.toString());

  opcionesDeTAP_0= signal<OpcionesDeEdicionENUM>(OpcionesDeEdicionENUM.VER_PERFIL)


  idUsuario = computed(()=> this.authStore.usuario()?.id!)


  constructor() {
  }

  ngOnInit() {

  }


  onTabChange(value: string | number | undefined) {
    if (value === undefined) return;

    const v = String(value);

    // si vuelvo/entro a Perfil => reseteo el "sub-estado" a VER_PERFIL
    if (v === this.tapEnum.TAP_0) {
      this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.VER_PERFIL);
    }

    this.activeTab.set(v);
  }



  onCloseEditarPerfil() {
    this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.VER_PERFIL);
  }


  onEditarPerfil(){
    this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.EDITAR_PERDIL);
  }

  onEditarPassword(){
    this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.EDITAR_PASSWORD);
  }

  get tapEnum(){
    return TapEnum
  }
  get opcionesDeEdicionENUM(){
    return OpcionesDeEdicionENUM;
  }


  onBack(){

  }

}
