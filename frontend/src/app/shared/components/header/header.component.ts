import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/auth.service';
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, MatToolbarModule, MatButtonModule],
  template: `
    <mat-toolbar color="primary">
      <button mat-button routerLink="/">Veículos</button>
      <button mat-button routerLink="/carrinho">Carrinho</button>
      <button *ngIf="isVendedor()" mat-button routerLink="/usuarios">Usuários</button>
      <span class="spacer"></span>
      <button mat-button (click)="logout()">Sair</button>
    </mat-toolbar>
  `,
  styles: [`
    .spacer {
      flex: 1 1 auto;
    }
  `]
})
export class HeaderComponent {
  constructor(private auth: AuthService) {}

  isVendedor(): boolean {
    return this.auth.getPerfil() === 'VENDEDOR';
  }

  logout() {
    this.auth.logout();
  }
}
