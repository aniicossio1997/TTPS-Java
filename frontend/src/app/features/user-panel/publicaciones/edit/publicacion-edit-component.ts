import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicacionFormComponent } from '../form/publicacion-form/publicacion-form';
import {
  Publicacion,
  PublicacionCreate,
  PublicacionUpdate,
} from '../../../../interfaces/publicacion.interface';
import { PublicacionesService } from '../../../../services/publicaciones.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-publicacion-edit',
  standalone: true,
  template: `
    <ng-container *ngIf="publicacion$ | async as publicacion">
      <app-publicacion-form
        [publicacionId]="publicacionId"
        [publicacionExistente]="publicacion"
        (formSubmit)="handleFormSubmit($event)"
      />
    </ng-container>
  `,
  imports: [CommonModule, PublicacionFormComponent],
})
export class PublicacionEditComponent implements OnInit {
  private publicacionesService = inject(PublicacionesService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  public publicacionId: number | null = null;
  public publicacion$!: Observable<Publicacion>;

  ngOnInit(): void {
    this.publicacion$ = this.route.paramMap.pipe(
      switchMap((params) => {
        const id = params.get('id');

        this.publicacionId = id ? +id : null;

        if (this.publicacionId) {
          return this.publicacionesService.getById(this.publicacionId);
        }
        return new Observable<Publicacion>();
      })
    );
  }

  public handleFormSubmit(publicacion: PublicacionUpdate): void {
    if (this.publicacionId === null) {
      console.error('Error: No se encontró ID para actualizar la publicación.');
      return;
    }

    console.log(
      `Datos de Publicación listos para actualizar (ID: ${this.publicacionId}):`,
      publicacion
    );

    this.publicacionesService.update(this.publicacionId, publicacion).subscribe({
      next: (publicacionActualizada) => {
        console.log('Publicación actualizada con éxito:', publicacionActualizada);

        this.router.navigate(['/app/publicaciones', 'detalle', publicacionActualizada.id]);
      },
      error: (err) => {
        console.error('Error al actualizar la publicación:', err);
      },
    });
  }
}
