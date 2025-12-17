import { Component, inject, signal, computed, ChangeDetectionStrategy, CUSTOM_ELEMENTS_SCHEMA, OnInit } from '@angular/core';
import { PerfilCard } from '../../../components/perfil-card/perfil-card';
import { TabsModule } from 'primeng/tabs';
import { ButtonModule } from 'primeng/button';
import { EditarPerfil } from '../../../components/editar-perfil/editar-perfil';
import { SpeedDialModule } from 'primeng/speeddial';
import { EditPassword } from '../../../components/edit-password/edit-password';
import { AuthStoreService } from '../../../store/auth.stored.service';
import { PulicacionesUser } from './pulicaciones-user/pulicaciones-user';
import { AvistamientosUser } from './avistamientos-user/avistamientos-user';
import { Dialog } from 'primeng/dialog';
import { PerfilByUserStoreService } from '../../../store/perfilByUser.stored.service';
import { UsuarioService } from '../../../services/usuario.service';
import { RouterLink } from '@angular/router';
import { MedallasSection } from "./medallas-section/medallas-section";

enum TapEnum{
  TAP_0="0", // -> PUBLICACIONES
  TAP_1="1",// -> UBICACIONES

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
  imports: [TabsModule, PerfilCard, ButtonModule, EditarPerfil, EditPassword, PulicacionesUser, AvistamientosUser, Dialog, RouterLink, MedallasSection],
  templateUrl: './perfil-section.html',
  styleUrl: './perfil-section.scss',
  schemas:[CUSTOM_ELEMENTS_SCHEMA],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers:[PerfilByUserStoreService, UsuarioService]
})
export class PerfilSection implements OnInit {

  private authStore= inject(AuthStoreService)

  readonly perfilStore =inject(PerfilByUserStoreService)

  modalOpen = false;

  activeTab = signal(this.tapEnum.TAP_0.toString());

  opcionesDeTAP_0= signal<OpcionesDeEdicionENUM>(OpcionesDeEdicionENUM.VER_PERFIL)

  selectedOption = signal<OpcionesDeEdicionENUM | null>(null);

  modalTitle = computed(() => {
    switch (this.selectedOption()) {
      case OpcionesDeEdicionENUM.EDITAR_PERDIL:
        return 'Editar perfil';
      case OpcionesDeEdicionENUM.EDITAR_PASSWORD:
        return 'Cambiar contraseña';
      default:
        return 'Edición';
    }
  });

  openModal(option: OpcionesDeEdicionENUM) {
    this.selectedOption.set(option);
    this.modalOpen = true;
  }

  closeModal() {
    this.modalOpen = false;
    this.selectedOption.set(null); // opcional (si querés “resetear”)
  }

  idUsuario = computed(()=> this.authStore.usuario()?.id!)


  constructor() {
  }

  ngOnInit() {
    this.perfilStore._getPerfil(this.idUsuario())
  }


  onTabChange(value: string | number | undefined) {
    if (value === undefined) return;

    const v = String(value);
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


  onSuccessEdit(){
    this.perfilStore._getPerfil(this.idUsuario())
  }

  get destinationUrl(): string {
    if (this.authStore.isAuthenticated()) {
      return '/app/publicaciones/crear';
    }
    return '/public/login';
  }

}
