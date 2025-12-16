import { Component, input, output } from '@angular/core';
import { EditarPerfil } from '../../../../components/editar-perfil/editar-perfil';

@Component({
  selector: 'app-editar-perfil-section',
  imports: [EditarPerfil],
  templateUrl: './editar-perfil-section.html',
  styleUrl: './editar-perfil-section.scss',
})
export class EditarPerfilSection {
  idUsuario = input.required<number>();


  onCloseEditarPerfil(){

  }
}
