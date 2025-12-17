import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedallasSection } from './medallas-section';

describe('MedallasSection', () => {
  let component: MedallasSection;
  let fixture: ComponentFixture<MedallasSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedallasSection]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MedallasSection);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
