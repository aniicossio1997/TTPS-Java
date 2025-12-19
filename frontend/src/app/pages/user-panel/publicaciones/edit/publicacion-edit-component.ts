import { Component, computed, inject, OnInit, signal } from '@angular/core';
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
import { ToastrService } from 'ngx-toastr';
import { ApiStatus } from '../../../../interfaces/local/EnumApiStatus.enum';

type PublicacionSubmitEvent = [PublicacionCreate, File[]];

@Component({
  selector: 'app-publicacion-edit',
  standalone: true,
  template: `
    <ng-container *ngIf="publicacion$ | async as publicacion">
      <app-publicacion-form
        [publicacionId]="publicacionId"
        [publicacionExistente]="publicacion"
        (formSubmit)="handleFormSubmit($event)"
        [statusForm]="_status()"
      />
    </ng-container>
  `,
  imports: [CommonModule, PublicacionFormComponent],
})
export class PublicacionEditComponent implements OnInit {
  private publicacionesService = inject(PublicacionesService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private readonly toastr = inject(ToastrService);

  public publicacionId: number | null = null;
  public publicacion$!: Observable<Publicacion>;

  public _status  = signal<ApiStatus>(ApiStatus.INIT);



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

  public handleFormSubmit(event: PublicacionSubmitEvent): void {


    if (this.publicacionId === null) {
      console.error('Error: No se encontró ID para actualizar la publicación.');
      return;
    }

    const [publicacion, imagenes] = event;
     this._status.set(ApiStatus.LOADING);



    this.publicacionesService.update(this.publicacionId, publicacion, imagenes).subscribe({
      next: (publicacionActualizada) => {

        this.toastr.success('Se modifico la publicacion con éxito', 'Éxito')
        this._status.set(ApiStatus.SUCCESS);

        this.router.navigate(['/app/publicaciones', 'detalle', publicacionActualizada.id]);
      },
      error: (err) => {
          this._status.set(ApiStatus.ERROR);
        console.error('Error al actualizar la publicación:', err);
      },
    });
  }
}
