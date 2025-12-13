import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../../common/sidebar/sidebar';

@Component({
  selector: 'app-layout-component',
  imports: [RouterOutlet, Sidebar],
  templateUrl: './layout-component.html',
  styleUrl: './layout-component.scss',
})
export class LayoutComponent {

}
