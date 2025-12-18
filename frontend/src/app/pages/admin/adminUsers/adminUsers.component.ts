import { UsuarioService } from './../../../services/usuario.service';
import { UsuarioSmall } from './../../../interfaces/usuario.interface.';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  DestroyRef,
  inject,
  Injectable,
  OnDestroy,
  OnInit,
  signal,
  ViewChild,
  ViewChildren,
  QueryList,
  HostListener,
} from '@angular/core';

import { Table, TableModule } from 'primeng/table';
import { Tag, TagModule } from 'primeng/tag';
import { RatingModule } from 'primeng/rating';
import { ButtonModule } from 'primeng/button';
import { PaginatorModule } from 'primeng/paginator';
import { ApiStatus } from '../../../interfaces/local/EnumApiStatus.enum';
import { ToastrService } from 'ngx-toastr';
import { UserAvatarComponent } from '../../../components/user-avatar/user-avatar';
import { UsuarioDetalleDTO } from '../../../interfaces/UsuarioDetalleDTO.interface';
import { EstadoUsuarioEnum } from '../../../interfaces/local/estadoUsuarioEnum';
import { Subscription } from 'rxjs';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { Menu } from 'primeng/menu';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Dialog } from 'primeng/dialog';
import { EditarPerfil } from '../../../components/editar-perfil/editar-perfil';
import { EditPassword } from '../../../components/edit-password/edit-password';
import { AuthStoreService } from '../../../store/auth.stored.service';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ManagementRoutes } from '../../../const/rutas.const';
import { CambiarEstadoUsuario } from '../../../components/cambiar-estado-usuario/cambiar-estado-usuario';
import { CambiarEstadoUsuarioStoreService } from '../../../store/cambiarEstadoUsuario.stored.service';

interface UsuariosStateStore {
  data: UsuarioSmall[];
  status: ApiStatus;
  error: string | null;
}

enum OpcionesDeEdicionENUM {
  VER_PERFIL = 'VER_PERFIL',
  EDITAR_PERDIL = 'EDITAR_PERDIL',
  EDITAR_PASSWORD = 'EDITAR_PASSWORD',
  CAMBIAR_ESTADO = 'CAMBIAR_ESTADO',
}

@Component({
  selector: 'app-adminUsers',
  imports: [
    ButtonModule,
    TagModule,
    CommonModule,
    PaginatorModule,
    RatingModule,
    TableModule,
    Tag,
    UserAvatarComponent,
    ConfirmDialog,
    Dialog,
    EditarPerfil,
    EditPassword,
    CambiarEstadoUsuario,

  ],
  templateUrl: './adminUsers.component.html',
  styleUrls: ['./adminUsers.component.scss'],
  providers: [UsuarioService, ConfirmationService, CambiarEstadoUsuarioStoreService],

  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminUsersComponent implements OnInit, OnDestroy {

  @ViewChild('dt') dt!: Table;
  private confirmationService = inject(ConfirmationService);
  private estadoUserStore = inject(CambiarEstadoUsuarioStoreService);

  private readonly toastr = inject(ToastrService);
  private readonly serviceUser = inject(UsuarioService);
  public authStore = inject(AuthStoreService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  modalOpen = false;
  selectedOption = signal<OpcionesDeEdicionENUM | null>(null);

  usuarioSelected = signal<UsuarioSmall | null>(null)

  itemsMenu: MenuItem[] = [];
  @ViewChild('menuCompartido') menuCompartido!: Menu;

  subs: Subscription = null!;

  public usuarioStore = signal<UsuariosStateStore>({
    data: [],
    status: ApiStatus.INIT,
    error: null,
  });

  usuarios = computed(() => this.usuarioStore().data);

  public isLoading = computed(() => this.usuarioStore().status == ApiStatus.LOADING);
  public isError = computed(() => this.usuarioStore().status == ApiStatus.ERROR);

  public isSuccess = computed(() => this.usuarioStore().status == ApiStatus.SUCCESS);

  public isNotFound = computed(() => this.usuarioStore().status == ApiStatus.NOT_FOUND);

  public avistamientos = computed(() => this.usuarioStore().data);


  private navSub: Subscription = new Subscription();

  constructor() {
        // Cerrar cualquier overlay al iniciar navegación

  }




  ngOnInit() {
    this.getUsuarios();

    // Cerrar todos los menús cuando hay navegación

  }

  getColorEstado(
    user: UsuarioSmall
  ): 'warn' | 'success' | 'secondary' | 'info' | 'danger' | 'contrast' {
    const estado = user.estado;
    switch (estado) {
      case EstadoUsuarioEnum.BAJA_VOLUNTARIA:
        return 'danger';
      case EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN:
        return 'danger';
      case EstadoUsuarioEnum.HABILITADO:
        return 'success';
    }
  }

  getLabelEstadoUsuario(user: UsuarioSmall) {
    const estado = user.estado;
    switch (estado) {
      case EstadoUsuarioEnum.BAJA_VOLUNTARIA:
        return 'Baja voluntaria';
      case EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN:
        return 'Bloqueado';
      case EstadoUsuarioEnum.HABILITADO:
        return 'Habilitado';
    }
  }

  ngOnDestroy() {
    this.subs?.unsubscribe();
    this.navSub?.unsubscribe();
  }

  getUsuarios() {
    this.usuarioStore.set({ ...this.usuarioStore(), status: ApiStatus.LOADING });

    this.subs?.unsubscribe(); // corta la anterior
    this.subs = new Subscription(); // recrea para poder usar add()
    this.subs = this.serviceUser.getUsuarios().subscribe({
      next: (resp) => {
        const users = resp.filter(user=> user.id!= this.authStore.usuario()?.id)
        this.usuarioStore.update((state) => ({
          ...state,
          data: users,
          status: ApiStatus.SUCCESS,
        }));
      },
      error: (errorMessage) => {
        this.usuarioStore.set({ ...this.usuarioStore(), status: ApiStatus.LOADING });
        this.usuarioStore.update((state) => ({
          ...state,
          data: [],
          status: ApiStatus.ERROR,
          error: errorMessage,
        }));
        this.toastr.error('Error al recuperar el perfil', 'Error en el servidor');
      },
    });
  }

// 2. Escuchamos clicks en CUALQUIER parte de la página
  @HostListener('document:click', ['$event'])
  clickout(event: Event) {
    // Si el usuario hace click en cualquier lado (que no sea el botón), cerramos
    if (this.openMenuId() !== null) {
      this.openMenuId.set(null) ;
    }
  }

  openModal(option: OpcionesDeEdicionENUM, usuario:UsuarioSmall) {
    this.usuarioSelected.set(usuario)
    this.selectedOption.set(option);
    this.modalOpen = true;
  }
  closeModal() {
    this.usuarioSelected.set(null)
    this.modalOpen = false;
    this.selectedOption.set(null); // opcional (si querés “resetear”)
  }
  onSuccessEdit() {
    this.closeModal()
    this.getUsuarios();
  }

  get opcionesDeEdicionENUM() {
    return OpcionesDeEdicionENUM;
  }
  get estadoUsuarioEnum() {
    return EstadoUsuarioEnum;
  }


  openMenuId =signal<number | null>(null);

      // Abrir/Cerrar menú
  toggleMenu(id: number, event: Event) {

    event.stopPropagation(); // Evita que el click llegue al backdrop inmediatamente
    if ( this.openMenuId() &&this.openMenuId() == id) {
      this.closeMenu();
    } else {
      this.openMenuId.set(id);
    }
  }

  // Cerrar cualquier menú abierto
  closeMenu() {
    this.openMenuId.set(null);
  }

    // --- TUS ACCIONES ---

  irAlDetalle(usuario: UsuarioSmall) {
    this.closeMenu(); // 1. Cerramos menú
    console.log('Navegando a detalle de:', usuario.nombre);
        if (usuario.id) {
            // Navegamos y forzamos el cierre del menú para evitar el bug visual
            this.router.navigate([`${ManagementRoutes.Admin}/${ManagementRoutes.Usuarios}`,usuario.id]);
            this.menuCompartido.hide();
        }
  }

  editarUsuario(usuario: UsuarioSmall) {
    this.closeMenu();
    this.openModal(OpcionesDeEdicionENUM.EDITAR_PERDIL, usuario);
    console.log('Abriendo modal editar para:', usuario.nombre);
    // this.openModal(...)
  }

  eliminarUsuario(usuario: UsuarioSmall) {
    this.closeMenu();
    this.confirmationEliminarUsuario(usuario);
    //this.openModal(OpcionesDeEdicionENUM.CAMBIAR_ESTADO, usuario);
  }

  confirmationEliminarUsuario(usuario: UsuarioSmall) {
    this.confirmationService.confirm({
      key: 'positionDialog',
      header: 'Bloquear usuario',
      message: '¿Estás seguro de bloquear al usuario?',
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
        label: 'Cancel',
        severity: 'secondary',
        text: true,
      },
      rejectButtonStyleClass: 'p-button-secondary',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.estadoUserStore._cambiarEstadoUsuario(usuario.id, EstadoUsuarioEnum.BLOQUEADO_POR_ADMIN, ()=>{ this.getUsuarios()});

      },
      reject: () => {
        // Lógica si dice que NO
      }
    });
  }

  confirmationHabilitarUsuario(usuario: UsuarioSmall) {
    this.confirmationService.confirm({
      key: 'positionDialog',
      header: 'Habilitar usuario',
      message: '¿Estás seguro de habilitar al usuario?',
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
        label: 'Cancel',
        severity: 'secondary',
        text: true,
      },
      rejectButtonStyleClass: 'p-button-secondary',
      acceptButtonStyleClass: '',
      accept: () => {
        this.estadoUserStore._cambiarEstadoUsuario(usuario.id, EstadoUsuarioEnum.HABILITADO, ()=>{ this.getUsuarios()});
      },
      reject: () => {
        // Lógica si dice que NO
      }
    });
  }

}
