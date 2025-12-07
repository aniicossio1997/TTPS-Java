/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { GeorefApiExternaService } from './georefApiExterna.service';

describe('Service: GeorefApiExterna', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GeorefApiExternaService]
    });
  });

  it('should ...', inject([GeorefApiExternaService], (service: GeorefApiExternaService) => {
    expect(service).toBeTruthy();
  }));
});
