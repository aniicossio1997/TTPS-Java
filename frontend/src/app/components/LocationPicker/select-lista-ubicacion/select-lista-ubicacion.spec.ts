import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectListaUbicacion } from './select-lista-ubicacion';

describe('SelectListaUbicacion', () => {
  let component: SelectListaUbicacion;
  let fixture: ComponentFixture<SelectListaUbicacion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectListaUbicacion]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectListaUbicacion);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
