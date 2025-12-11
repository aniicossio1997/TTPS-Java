import { Component, OnInit, signal } from '@angular/core';

// Interfaz para definir la estructura de una publicación
interface MascotaPost {
  id: number;
  imagen: string;
  estado: string;
  fecha: string;
  color: string;
  tamano: string;
  comentario: string;
  ubicacion: string;
}

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }
    // Utilizamos Signals para el manejo del estado (Modern Angular)
  posts = signal<MascotaPost[]>([
    {
      id: 1,
      imagen: 'https://images.unsplash.com/photo-1517849845537-4d257902454a?q=80&w=800&auto=format&fit=crop', // Imagen de perro Jack Russell o similar
      estado: 'BUSCANDO A MI MASCOTA',
      fecha: '09/10/2025',
      color: 'Blanco y Marrón',
      tamano: 'Mediano',
      comentario: 'Se perdió cerca del parque central. Tiene un collar rojo con chapita. Responde al nombre de "Toby". Es muy amigable pero asustadizo con los ruidos fuertes.',
      ubicacion: 'San Lorenzo, La Plata'
    },
     {
      id: 2,
      imagen: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?q=80&w=800&auto=format&fit=crop',
      estado: 'BUSCANDO A MI MASCOTA',
      fecha: '05/10/2025',
      color: 'Negro',
      tamano: 'Pequeño',
      comentario: 'Perrito negro, visto por última vez en la esquina de la panadería. Por favor ayudar.',
      ubicacion: 'Centro'
    }
  ]);

}
