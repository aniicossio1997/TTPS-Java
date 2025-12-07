import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { Sidebar } from './sidebar/sidebar';

@Component({
  selector: 'app-adminLayout',
  imports: [
    ButtonModule, AvatarModule,
    RouterOutlet,
    Sidebar
],
  templateUrl: './adminLayout.component.html',
  styleUrls: ['./adminLayout.component.scss']
})
export class AdminLayoutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
