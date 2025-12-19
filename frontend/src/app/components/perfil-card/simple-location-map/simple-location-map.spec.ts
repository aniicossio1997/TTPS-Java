import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleLocationMap } from './simple-location-map';

describe('SimpleLocationMap', () => {
  let component: SimpleLocationMap;
  let fixture: ComponentFixture<SimpleLocationMap>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SimpleLocationMap]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SimpleLocationMap);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
