import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CambiarEstadoUsuario } from './cambiar-estado-usuario';

describe('CambiarEstadoUsuario', () => {
  let component: CambiarEstadoUsuario;
  let fixture: ComponentFixture<CambiarEstadoUsuario>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CambiarEstadoUsuario]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CambiarEstadoUsuario);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
