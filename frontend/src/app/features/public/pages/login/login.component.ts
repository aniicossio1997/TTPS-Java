import { Component, inject, OnInit } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
  FormsModule
} from '@angular/forms';

// PrimeNG
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { AbstractControl } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { DividerModule } from 'primeng/divider';


interface Option {
  label: string;
  value: string;
}

type RegisterForm = {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  phone: FormControl<string>;
  city: FormControl<string>;
  neighborhood: FormControl<string>;
  password: FormControl<string>;
};

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    InputTextModule,

    PasswordModule,
    ButtonModule,
    CardModule,
    PasswordModule,
    FormsModule,
    DividerModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  ngOnInit(): void {

  }

  // 🔹 Inyectamos el FormBuilder con el nuevo patrón
  private readonly fb = inject(FormBuilder).nonNullable;

  // 🔹 Creamos el FormGroup con el método group() del FormBuilder
  //    Todos los controles son no-nullable gracias a .nonNullable
  readonly registerForm: FormGroup<RegisterForm> = this.fb.group({
    firstName: this.fb.control('', [Validators.required]),
    lastName: this.fb.control('', [Validators.required]),
    email: this.fb.control('', [Validators.required, Validators.email]),
    phone: this.fb.control(''),
    city: this.fb.control('', [Validators.required]),
    neighborhood: this.fb.control('', [Validators.required]),
    password: this.fb.control('', [Validators.required, Validators.minLength(6)]),
  });

  readonly cities: Option[] = [
    { label: 'Buenos Aires', value: 'ba' },
    { label: 'Córdoba', value: 'cb' },
    { label: 'Rosario', value: 'ros' }
  ];

  readonly neighborhoods: Option[] = [
    { label: 'Centro', value: 'centro' },
    { label: 'Norte', value: 'norte' },
    { label: 'Sur', value: 'sur' }
  ];

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    // getRawValue() para mantener bien el tipo
    const data = this.registerForm.getRawValue();
    console.log('Form value', data);
    // acá llamás al backend
  }

}
