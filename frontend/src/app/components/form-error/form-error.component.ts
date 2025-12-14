import { Component, Input } from '@angular/core';
import { AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-form-error',
  templateUrl: './form-error.component.html'
})
export class FormErrorComponent {
  @Input() control!: AbstractControl | null;

  /**
   * Mensajes custom opcionales
   * Ej: { required: 'Este campo es obligatorio' }
   */
  @Input() messages: Partial<Record<string, string>> = {};

  shouldShowError(): boolean {
    return !!(
      this.control &&
      this.control.invalid &&
      (this.control.dirty || this.control.touched)
    );
  }

  get errorMessage(): string {
    if (!this.control?.errors) return '';

    const errors = this.control.errors;

    // 1️⃣ si hay mensaje custom, gana
    for (const key of Object.keys(errors)) {
      if (this.messages[key]) {
        return this.messages[key]!;
      }
    }

    // 2️⃣ mensajes por defecto
    return this.getDefaultMessage(errors);
  }

  private getDefaultMessage(errors: ValidationErrors): string {
    if (errors['required']) {
      return 'Este campo es obligatorio.';
    }

    if (errors['email']) {
      return 'El formato del email no es válido.';
    }

    if (errors['minlength']) {
      return `Debe tener al menos ${errors['minlength'].requiredLength} caracteres.`;
    }

    if (errors['maxlength']) {
      return `Debe tener como máximo ${errors['maxlength'].requiredLength} caracteres.`;
    }

    if (errors['min']) {
      return `El valor mínimo es ${errors['min'].min}.`;
    }

    if (errors['max']) {
      return `El valor máximo es ${errors['max'].max}.`;
    }

    if (errors['pattern']) {
      return 'El formato ingresado no es válido.';
    }

    return 'El valor ingresado no es válido.';
  }
}