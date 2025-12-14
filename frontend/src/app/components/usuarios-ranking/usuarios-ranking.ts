import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';

// PrimeNG Modules
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { AvatarModule } from 'primeng/avatar';
import { TagModule } from 'primeng/tag';
import { PublicService } from '../../services/public.service';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-usuarios-ranking',
  standalone: true,
  imports: [
    CommonModule,
    CardModule,
    SkeletonModule,
    AvatarModule,
    TagModule,
    DividerModule
  ],
  templateUrl: './usuarios-ranking.html',
})
export class UsuariosRanking {

  private publicService = inject(PublicService);

  public ranking = toSignal(
    this.publicService.getRankingUsuarios(),
    { initialValue: null }
  );

  public avatarUrl = signal('https://cdn.primefaces.org/images/avatar/amyelsner.png');

  public skeletonItems = Array(10).fill(0);

  getPuestoSeverity(index: number): 'warn' | 'info' | 'secondary' {
    if (index === 0) return 'warn';
    if (index === 1) return 'info';
    if (index === 2) return 'info';
    return 'secondary';
  }
}