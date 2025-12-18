import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserLogueado } from './user-logueado';

describe('UserLogueado', () => {
  let component: UserLogueado;
  let fixture: ComponentFixture<UserLogueado>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserLogueado]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserLogueado);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
