import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvistamientosUser } from './avistamientos-user';

describe('AvistamientosUser', () => {
  let component: AvistamientosUser;
  let fixture: ComponentFixture<AvistamientosUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvistamientosUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvistamientosUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
