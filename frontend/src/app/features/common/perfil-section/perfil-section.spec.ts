import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilSection } from './perfil-section';

describe('PerfilSection', () => {
  let component: PerfilSection;
  let fixture: ComponentFixture<PerfilSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilSection]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
