import { Component, OnInit, input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { CarouselModule } from 'primeng/carousel';
import { SkeletonModule } from 'primeng/skeleton';
import { Observable, BehaviorSubject, switchMap, finalize } from 'rxjs';
import { AvistamientoService } from '../../../../services/avistamiento.service';
import { Avistamiento, AvistamientoFilter } from '../../../../interfaces/avistamiento.interface';

@Component({
  selector: 'app-avistamientos-list',
  templateUrl: './avistamientos-list.html',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    TagModule,
    ButtonModule,
    DividerModule,
    CarouselModule,
    SkeletonModule,
  ],
})
export class AvistamientosList implements OnInit {
  private avistamientoService = inject(AvistamientoService);

  publicacionId = input<number | null>(null);
  usuarioId = input<number | null>(null);

  avistamientos$!: Observable<Avistamiento[]>;
  loading: boolean = true;

  constructor() {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    const filter: AvistamientoFilter = {};
    const publicacionId = this.publicacionId();
    const usuarioId = this.usuarioId();
    if (publicacionId) {
      filter.publicacionId = publicacionId;
    }

    if (usuarioId) {
      filter.usuarioId = usuarioId;
    }

    this.loading = true;
    this.avistamientos$ = this.avistamientoService
      .getFiltered(filter)
      .pipe(finalize(() => (this.loading = false)));
  }

  recargar(): void {
    this.load();
  }

  getSeverity(agradecimiento: boolean): 'success' | 'info' {
    return agradecimiento ? 'success' : 'info';
  }
}
