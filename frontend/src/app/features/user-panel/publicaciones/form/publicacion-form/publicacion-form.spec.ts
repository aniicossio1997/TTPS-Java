import { PublicacionFormComponent } from './publicacion-form';
import { ComponentFixture, TestBed } from '@angular/core/testing';


describe('PublicacionForm', () => {
  let component: PublicacionFormComponent;
  let fixture: ComponentFixture<PublicacionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicacionFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PublicacionFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
