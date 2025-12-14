import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilCard } from './perfil-card';

describe('PerfilCard', () => {
  let component: PerfilCard;
  let fixture: ComponentFixture<PerfilCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PerfilCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
