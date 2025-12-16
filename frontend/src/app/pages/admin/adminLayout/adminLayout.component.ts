
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Sidebar } from '../../../components/sidebar/sidebar';

@Component({
  selector: 'app-adminLayout',
  imports: [
    ButtonModule, AvatarModule,
    RouterOutlet,
    Sidebar

],
  templateUrl: './adminLayout.component.html',
  styleUrls: ['./adminLayout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AdminLayoutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
