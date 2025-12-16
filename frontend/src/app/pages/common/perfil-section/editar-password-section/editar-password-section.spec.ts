import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarPasswordSection } from './editar-password-section';

describe('EditarPasswordSection', () => {
  let component: EditarPasswordSection;
  let fixture: ComponentFixture<EditarPasswordSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarPasswordSection]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarPasswordSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
