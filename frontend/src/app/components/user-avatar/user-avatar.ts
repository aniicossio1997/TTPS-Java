import { ChangeDetectionStrategy, Component, computed, effect, Input, input, OnInit, signal } from '@angular/core';
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
        [size]="size()"
      />

      @if(showName()){
      <span  class="truncate font-bold text-gray-800">
        {{ usuario().nombre }} {{ usuario().apellido }}
      </span>
      }
    </div>
  `,
  styles: [],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserAvatarComponent implements OnInit {

  //@Input({ required: true }) usuario!: UsuarioSmall;
  usuario = input.required<UsuarioSmall>()

 showName=input<boolean>(false);
 size =input<'normal' | 'large' | 'xlarge'>('normal');



    /** Iniciales solo si no hay imagen */
  avatarLabel = computed(() => {
    if (this.usuario().foto?.url) return '';

    const n = this.usuario().nombre?.[0]?.toUpperCase() ?? '';
    const a = this.usuario().apellido?.[0]?.toUpperCase() ?? '';
    return n + a;
  });

  public avatarImage = computed(() => {
    const url = this.usuario().foto?.url;
    if (!url) return undefined;

    // TRUCO: Agregamos ?t=tiempo_actual
    // Esto genera: ".../foto.jpg?t=17344551231"
    // El navegador ve una URL "nueva" y la descarga sí o sí.
    return `${url}?t=${new Date().getTime()}`;
  });

  constructor(){

  }

  ngOnInit(): void {


  }
}
