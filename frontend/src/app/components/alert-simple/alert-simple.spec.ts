import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertSimple } from './alert-simple';

describe('AlertSimple', () => {
  let component: AlertSimple;
  let fixture: ComponentFixture<AlertSimple>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertSimple]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlertSimple);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
