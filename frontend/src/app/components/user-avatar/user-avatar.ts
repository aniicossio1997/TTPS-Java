import { Component, Input, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AvatarModule } from 'primeng/avatar';
import { UsuarioSmall } from '../../interfaces/usuario.interface.';

@Component({
  selector: 'app-user-avatar',
  standalone: true,
  imports: [CommonModule, AvatarModule],
  template: `
    <div class="flex items-center" [ngClass]="{ 'space-x-2': showName }">
      <p-avatar
        [image]="avatarImage()"
        [label]="avatarLabel()"
        class="flex-shrink-0 bg-indigo-100 text-indigo-800 text-xs"
        style="background-color: #ece9fc; color: #2a1261"
        shape="circle"
        [size]="size"
      />

      <span *ngIf="showName" class="truncate font-bold text-gray-800">
        {{ usuario.nombre }} {{ usuario.apellido }}
      </span>
    </div>
  `,
  styles: [],
})
export class UserAvatarComponent implements OnInit {
  @Input({ required: true }) usuario!: UsuarioSmall;
  @Input() showName: boolean = false;
  @Input() size: 'normal' | 'large' | 'xlarge' = 'normal';

  public avatarLabel = signal<string>('');
  public avatarImage = signal<string | undefined>(undefined);

  ngOnInit(): void {
    if (!this.usuario) return;

    console.log(this.usuario.foto?.url);
    this.avatarImage.set(this.usuario.foto?.url);

    if (this.usuario.foto?.url) return;

    const inicialNombre = this.usuario.nombre ? this.usuario.nombre[0].toUpperCase() : '';
    const inicialApellido = this.usuario.apellido ? this.usuario.apellido[0].toUpperCase() : '';
    this.avatarLabel.set(inicialNombre + inicialApellido);
  }
}
