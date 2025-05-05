import { MatFormFieldModule } from '@angular/material/form-field';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth.service';
import { CommonModule } from '@angular/common';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule
  ],
})
export class LoginComponent {
  form;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  error: string | null = null;

  onSubmit() {
    if (this.form.invalid) return;

    const credentials = {
      login: this.form.value.login || '',
      password: this.form.value.password || ''
    };
    this.auth.login(credentials).subscribe({
      next: (user) => {
        this.auth.setUser(user);
        this.router.navigate(['/']);
      },
      error: () => {
        this.error = 'Login ou senha inválidos';
      }
    });
  }
}
