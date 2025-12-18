import { UsuarioService } from './../../../services/usuario.service';
import { UsuarioSmall } from './../../../interfaces/usuario.interface.';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  Injectable,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';

import { TableModule } from 'primeng/table';
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
import { MenuItem } from 'primeng/api';
import { Menu } from 'primeng/menu';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { Dialog } from 'primeng/dialog';
import { EditarPerfil } from '../../../components/editar-perfil/editar-perfil';
import { EditPassword } from '../../../components/edit-password/edit-password';
import { AuthStoreService } from '../../../store/auth.stored.service';
import { Router } from '@angular/router';

interface UsuariosStateStore {
  data: UsuarioSmall[];
  status: ApiStatus;
  error: string | null;
}

enum OpcionesDeEdicionENUM {
  VER_PERFIL = 'VER_PERFIL',
  EDITAR_PERDIL = 'EDITAR_PERDIL',
  EDITAR_PASSWORD = 'EDITAR_PASSWORD',
  ELIMINAR_PERFIL = 'ELIMINAR_PERFIL',
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
    Menu,
    ConfirmDialog,
    Dialog,
    EditarPerfil,
    EditPassword,
  ],
  templateUrl: './adminUsers.component.html',
  styleUrls: ['./adminUsers.component.scss'],
  providers: [UsuarioService],
  standalone: true,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminUsersComponent implements OnInit, OnDestroy {
  private readonly toastr = inject(ToastrService);
  private readonly serviceUser = inject(UsuarioService);
  public authStore = inject(AuthStoreService);

   private router = inject(Router);

  modalOpen = false;
  selectedOption = signal<OpcionesDeEdicionENUM | null>(null);
  idUsuarioSelected = signal<number |null>(null)

  itemsMenu: MenuItem[] = [];

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

  constructor() {}

  ngOnInit() {
    this.getUsuarios();
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

  async onMenu(menu: Menu, usuario: UsuarioSmall, $event: any) {
    menu.toggle($event);
    this.itemsMenu = [];
    this.itemsMenu = [
      {
        label: 'Opciones',
        items: [
          {
            label: 'Ir al detalle',
            icon: 'pi pi-eye',
            routerLink: ['/admin/usuarios', usuario.id],
            command: async () => {
               menu.hide();
               await this.router.navigate(['/admin/usuarios', usuario.id]);
            },
          },
          {
            label: 'Editar',
            icon: 'pi pi pi-pencil',
            command: () => {
              this.openModal(this.opcionesDeEdicionENUM.EDITAR_PERDIL, usuario)
            },
          },
          // {
          //   label: 'Cambiar contraseña',
          //   icon: 'pi pi pi-key',
          //   command: () => {
          //     this.openModal(this.opcionesDeEdicionENUM.EDITAR_PASSWORD)
          //   }

          // },
          {
            label: 'Cambiar estado',
            icon: 'pi pi-cog',
            command: () => {
              //this.confirmationEliminarUsuario()
            },
          },
        ],
      },
    ];

  }

  openModal(option: OpcionesDeEdicionENUM, usuario:UsuarioSmall) {
    this.idUsuarioSelected.set(usuario.id)
    this.selectedOption.set(option);
    this.modalOpen = true;
  }
  closeModal() {
    this.idUsuarioSelected.set(null)
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

    generateMenu(item: any, menu: Menu, event: any) {
    menu.toggle(event);

    this.itemsMenu = [
      {
        label: 'Detalle',
        icon: 'pi pi-eye',
        command: () => {
            this.router.navigate(['/admin/usuarios', item.id]);
        },
      },
      {
        label: 'Eliminar',
        icon: 'pi pi-trash',
        command: () => {
          //this.delete(item);
        },
      },
    ];
  }

}
