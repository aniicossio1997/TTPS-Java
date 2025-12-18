import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicacionFormComponent } from '../form/publicacion-form/publicacion-form';
import { PublicacionCreate } from '../../../../interfaces/publicacion.interface';
import { PublicacionesService } from '../../../../services/publicaciones.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

type PublicacionSubmitEvent = [PublicacionCreate, File[]];

@Component({
  selector: 'app-publicacion-create',
  standalone: true,
  template: `
    <app-publicacion-form
      [publicacionId]="null"
      [publicacionExistente]="null"
      (formSubmit)="handleFormSubmit($event)"
    />
  `,
  imports: [CommonModule, PublicacionFormComponent],
})
export class PublicacionCreateComponent {
  private publicacionesService = inject(PublicacionesService);
  private router = inject(Router);
  private readonly toastr = inject(ToastrService);

  public handleFormSubmit(event: PublicacionSubmitEvent): void {

    const [publicacion, imagenes] = event;


    this.publicacionesService.create(publicacion, imagenes).subscribe({
      next: (publicacionCreada) => {

        this.toastr.success('Se creo la publicacion con éxito', 'Éxito')
        this.router.navigate(['/app/publicaciones', 'detalle', publicacionCreada.id]);
      },
      error: (err) => {
        console.error('Error al crear la publicación:', err);
      },
    });
  }
}
