import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PulicacionesUser } from './pulicaciones-user';

describe('PulicacionesUser', () => {
  let component: PulicacionesUser;
  let fixture: ComponentFixture<PulicacionesUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PulicacionesUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PulicacionesUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
