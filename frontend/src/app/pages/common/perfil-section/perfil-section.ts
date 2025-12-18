import {
  Component,
  inject,
  signal,
  computed,
  ChangeDetectionStrategy,
  CUSTOM_ELEMENTS_SCHEMA,
  OnInit,
  effect,
  Input,
  input,
} from '@angular/core';
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
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MedallasSection } from './medallas-section/medallas-section';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { Menu } from 'primeng/menu';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { toSignal } from '@angular/core/rxjs-interop';
import { UsuarioDetalleDTO } from '../../../interfaces/UsuarioDetalleDTO.interface';
import { CambiarEstadoUsuarioStoreService } from '../../../store/cambiarEstadoUsuario.stored.service';
import { EstadoUsuarioEnum } from '../../../interfaces/local/estadoUsuarioEnum';
import { TagModule } from 'primeng/tag';

enum TapEnum {
  TAP_0 = '0', // -> PUBLICACIONES
  TAP_1 = '1', // -> UBICACIONES
}

const TAPS_CONST = {
  perfil: 'perfil',
  publicaciones: 'publicaciones',
  avistamientos: 'avistamientos',
} as const;

enum OpcionesDeEdicionENUM {
  VER_PERFIL = 'VER_PERFIL',
  EDITAR_PERDIL = 'EDITAR_PERDIL',
  EDITAR_PASSWORD = 'EDITAR_PASSWORD',
  ELIMINAR_PERFIL = 'ELIMINAR_PERFIL',
}

@Component({
  selector: 'app-perfil-section',
  imports: [
    TabsModule,
    PerfilCard,
    ButtonModule,
    EditarPerfil,
    EditPassword,
    PulicacionesUser,
    AvistamientosUser,
    Dialog,
    RouterLink,
    MedallasSection,
    CommonModule,
    RouterLink,
    //Menu,
    ConfirmDialog,
    TagModule
  ],
  templateUrl: './perfil-section.html',
  styleUrl: './perfil-section.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [PerfilByUserStoreService, UsuarioService, ConfirmationService, CambiarEstadoUsuarioStoreService],
})
export class PerfilSection implements OnInit {
  //@Input({ required: true }) userId!: string;
  private estadoUserStore = inject(CambiarEstadoUsuarioStoreService);
  reLoadPerfilCard =signal<boolean>(false);

  usuarioId = input.required<string>();

  public authStore = inject(AuthStoreService);

  private route = inject(ActivatedRoute);

  private paramMap = toSignal(this.route.paramMap);

  readonly idUser = computed(() => Number(this.paramMap()?.get('usuarioId')));

  userIdNumber = computed(()=> Number(this.usuarioId()))

  soyYoQuienVeMiPerfil = computed(()=> this.authStore.usuario()?.id== this.userIdNumber())

  tengoElPerfilHabilitado= computed (()=>{
    return this.perfilStore.perfilDeUsuario()?.estado==EstadoUsuarioEnum.HABILITADO
  })

  private confirmationService = inject(ConfirmationService);
  readonly perfilStore = inject(PerfilByUserStoreService);
  items: MenuItem[]=[];

  modalOpen = false;

  activeTab = signal(this.tapEnum.TAP_0.toString());

  opcionesDeTAP_0 = signal<OpcionesDeEdicionENUM>(OpcionesDeEdicionENUM.VER_PERFIL);

  selectedOption = signal<OpcionesDeEdicionENUM | null>(null);

  public authRuta = computed(() => this.authStore.baseUrl())

  openModal(option: OpcionesDeEdicionENUM) {
    this.selectedOption.set(option);
    this.modalOpen = true;
  }

  closeModal() {
    this.modalOpen = false;
    this.selectedOption.set(null); // opcional (si querés “resetear”)
  }

  canAgregarPublicaciones(){
    return this.perfilStore.perfilDeUsuario()?.id== this.authStore.usuario()?.id
  }

  canViewActions(){

    return (
      this.perfilStore.perfilDeUsuario()?.id == this.authStore.usuario()?.id
    && this.perfilStore.perfilDeUsuario()?.estado==EstadoUsuarioEnum.HABILITADO
    ) || this.authStore.isAdmin();
  }



  constructor() {
    effect(()=>{
      console.log(this.idUser())
    })
   }

  ngOnInit() {


    this.perfilStore._getPerfil(Number(this.usuarioId()));
  }

  onTabChange(value: string | number | undefined) {
    if (value === undefined) return;

    const v = String(value);
    this.activeTab.set(v);
  }

  onCloseEditarPerfil() {
    this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.VER_PERFIL);
  }

  onEditarPerfil() {
    this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.EDITAR_PERDIL);
  }

  onEditarPassword() {
    this.opcionesDeTAP_0.set(OpcionesDeEdicionENUM.EDITAR_PASSWORD);
  }

  get tapEnum() {
    return TapEnum;
  }
  get opcionesDeEdicionENUM() {
    return OpcionesDeEdicionENUM;
  }

  onSuccessEdit() {
    this.closeModal()
    this.perfilStore._getPerfil(Number(this.usuarioId()));
  }

  get destinationUrl(): string {
    if (this.authStore.isAuthenticated() && this.authStore.isAdmin()) {
      return '/admin/publicaciones/crear';
    }
    if (this.authStore.isAuthenticated() && !this.authStore.isAdmin()) {
      return '/app/publicaciones/crear';
    }
    return '/public/login';
  }

  // Ejemplo: Caso confirmación (Si/No)
  onConfirmationEliminarUsuario() {
    let mensaje = {
      mansaje: this.authStore.isAdmin() ? '¿Estás seguro de bloquear al usuario?' : '¿Estás seguro de darse se baja?',
      header: this.authStore.isAdmin() ? 'Bloquear usuario' : 'Darse de baja',
      estado: this.authStore.isAdmin() ? EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN : EstadoUsuarioEnum.BAJA_VOLUNTARIA
    }

    this.confirmationService.confirm({
      key: 'positionDialog',
      header: mensaje.header,
      message: mensaje.mansaje,
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
        label: 'Cancel',
        severity: 'secondary',
        text: true,
      },
      rejectButtonStyleClass: 'p-button-secondary',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.estadoUserStore._cambiarEstadoUsuario(this.perfilStore.perfilDeUsuario()!.id,mensaje.estado, ()=>{
           this.perfilStore._getPerfil(Number(this.usuarioId())); });


      },
      reject: () => {
        // Lógica si dice que NO
      }
    });
  }


  get EstadoUsuarioEnum() {
    return EstadoUsuarioEnum;
  }
}
