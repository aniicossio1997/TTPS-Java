import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarPerfilSection } from './editar-perfil-section';

describe('EditarPerfilSection', () => {
  let component: EditarPerfilSection;
  let fixture: ComponentFixture<EditarPerfilSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarPerfilSection]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarPerfilSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
