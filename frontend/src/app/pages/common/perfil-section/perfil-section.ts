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
    Menu,
    ConfirmDialog
  ],
  templateUrl: './perfil-section.html',
  styleUrl: './perfil-section.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [PerfilByUserStoreService, UsuarioService, ConfirmationService],
})
export class PerfilSection implements OnInit {
  //@Input({ required: true }) userId!: string;

  usuarioId = input.required<string>();

  public authStore = inject(AuthStoreService);

  private route = inject(ActivatedRoute);

  private paramMap = toSignal(this.route.paramMap);

  readonly idUser = computed(() => Number(this.paramMap()?.get('idUser')));

  userIdNumber = computed(()=> Number(this.usuarioId()))

  private confirmationService = inject(ConfirmationService);
  readonly perfilStore = inject(PerfilByUserStoreService);
  items: MenuItem[] | undefined;

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

  canEditarPerfil(){

    return this.perfilStore.perfilDeUsuario()?.id == this.authStore.usuario()?.id || this.authStore.isAdmin()
  }



  constructor() {
    effect(()=>{
      console.log(this.idUser())
    })
   }

  ngOnInit() {
    this.initMenu()

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
  confirmationEliminarUsuario() {
    this.confirmationService.confirm({
      key: 'positionDialog',
      header: 'Dar de baja',
      message: '¿Estás seguro de darse de baja?',
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
        label: 'Cancel',
        severity: 'secondary',
        text: true,
      },
      rejectButtonStyleClass: 'p-button-secondary',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        // Lógica si dice que SÍ
      },
      reject: () => {
        // Lógica si dice que NO
      }
    });
  }

  private initMenu() {
    this.items = [
      {
        label: 'Opciones',
        items: [
          {
            label: 'Editar',
            icon: 'pi pi pi-pencil',
            command: () => {
              this.openModal(this.opcionesDeEdicionENUM.EDITAR_PERDIL)
            }
          },
          {
            label: 'Cambiar contraseña',
            icon: 'pi pi pi-key',
            command: () => {
              this.openModal(this.opcionesDeEdicionENUM.EDITAR_PASSWORD)
            }

          },
          {
            label: 'Eliminar perfil',
            icon: 'pi pi-trash',
            command: () => {
              this.confirmationEliminarUsuario()
            }
          }
        ]
      }
    ];
  }


}
