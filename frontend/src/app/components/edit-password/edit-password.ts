import { Component, computed, inject, input, output, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { MessageModule } from 'primeng/message';
import { PasswordModule } from 'primeng/password';
import { Tooltip } from 'primeng/tooltip';
import { UsuarioService } from '../../services/usuario.service';
import { RestablecerPasswordRequest } from '../../interfaces/restablecerPasswordRequest.interface';
import { ToastrService } from 'ngx-toastr';
import { ApiStatus } from '../../interfaces/local/EnumApiStatus.enum';


function passwordMatchValidator(form: AbstractControl) {
  const passwordNueva = form.get('passwordNueva')?.value;
  const confirm = form.get('confirmPassword')?.value;

  if (passwordNueva !== confirm) {
    form.get('confirmPassword')?.setErrors({ mismatch: true });
  } else {
    const errors = form.get('confirmPassword')?.errors;
    if (errors) {
      delete errors['mismatch'];
      if (Object.keys(errors).length === 0) {
        form.get('confirmPassword')?.setErrors(null);
      }
    }
  }

  return null;
}

type RegisterForm = {

  passwordOld: FormControl<string>;
  passwordNueva: FormControl<string>;
  confirmPassword: FormControl<string>;
};

@Component({
  selector: 'app-edit-password',
  imports: [
    ButtonModule, ReactiveFormsModule,
    PasswordModule,MessageModule, DividerModule
  ],
  templateUrl: './edit-password.html',
  styleUrl: './edit-password.scss',
})
export class EditPassword {
  private readonly fb = inject(FormBuilder).nonNullable;
  private readonly _serviceUsuario = inject(UsuarioService);
  private readonly toast = inject(ToastrService);


  idUsuario= input.required<number>()
  onCloseEditarPerfil = output<void>();
  onSuccessEdit = output<void>();

  public mensajeError = signal<string>('')
  private _status = signal<ApiStatus>(ApiStatus.INIT);

  readonly isLoading = computed(() => this._status() === ApiStatus.LOADING);
  readonly isSuccess = computed(() => this._status() === ApiStatus.SUCCESS);
  readonly isError = computed(() => this._status() === ApiStatus.ERROR);

  readonly formPassword: FormGroup<RegisterForm> = this.fb.group(
    {

      passwordOld: this.fb.control('', [Validators.required,]),
      passwordNueva: this.fb.control('', [Validators.required, Validators.minLength(6)]),
      confirmPassword: this.fb.control('', [Validators.required]),
    },
    { validators: passwordMatchValidator }
  );

  onSubmit(){
    this._status.set(ApiStatus.LOADING)
    this.mensajeError.set('')
    this._serviceUsuario.cambiarPassword(this.idUsuario(), {
      confirmarPassword:this.formPassword.value!.confirmPassword!,
      nuevoPassword:this.formPassword.value.passwordNueva!,
      passwordOld:this.formPassword.value.passwordOld!
    }).subscribe({
    next: (res) => {
      if (res.ok) {
        this._status.set(ApiStatus.SUCCESS)
        this.toast.success(res.message);
        this.formPassword.reset()
        this.onCloseEditarPerfilLocal()
      }
    },
    error: (err) => {
       this.mensajeError.set(err.error?.message)
      this._status.set(ApiStatus.ERROR)
      this.toast.error(err.error?.message ?? 'Error inesperado, Verifique la contraseña actual sea la correcta, y que la nueva tenga más de 6 caracteres');
    }
  });
  }

  isInvalid(controlName: string) {
    const control = this.formPassword.get(controlName);
    return control?.invalid && (control.touched || control.dirty);
  }

  isInvalidTOBTN(controlName: string) {
    const control = this.formPassword.get(controlName);
    return control?.invalid;
  }

  onCloseEditarPerfilLocal(){
    this.onCloseEditarPerfil.emit()
  }

}
