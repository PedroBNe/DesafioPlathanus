import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface UsuarioAutenticado {
  login: string;
  name: string;
  token: string;
  isAdmin: boolean;
  authenticated: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = 'http://localhost:8080/usuario';
  private currentUser: UsuarioAutenticado | null = null;

  constructor(private http: HttpClient, private router: Router) {}

  isBrowser(): boolean {
    return typeof window !== 'undefined' && !!window.localStorage;
  }

  login(credentials: { login: string; password: string }) {
    return this.http.post<UsuarioAutenticado>(`${this.baseUrl}/autenticar`, credentials);
  }

  setUser(user: UsuarioAutenticado) {
    this.currentUser = user;
    localStorage.setItem('token', user.token);
    localStorage.setItem('perfil', user.isAdmin ? 'VENDEDOR' : 'CLIENTE');
    localStorage.setItem('login', user.login);
  }

  getUsername(): string | null {
    return this.currentUser?.login ?? localStorage.getItem('login');
  }

  getToken() {
    return this.isBrowser() ? localStorage.getItem('token') : null;
  }

  getPerfil() {
    return this.isBrowser() ? localStorage.getItem('perfil') : null;
  }

  isLoggedIn() {
    return !!this.getToken();
  }

  logout() {
    if (this.isBrowser()) {
      localStorage.clear();
    }
    this.router.navigate(['/login']);
  }
}
