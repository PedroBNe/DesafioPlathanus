import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/auth.service';

interface Usuario {
  cpf: string;
  name: string;
  login: string;
  role: string;
}

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  displayedColumns: string[] = ['cpf', 'name', 'login', 'role'];
  dataSource: Usuario[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    if (this.auth.getPerfil() !== 'VENDEDOR') {
      this.router.navigate(['/']);
      return;
    }

    this.http.get<Usuario[]>('http://localhost:8080/usuario/list')
      .subscribe({
        next: (res) => this.dataSource = res,
        error: () => alert('Erro ao carregar usuários')
      });
  }

  voltar() {
    this.router.navigate(['/']);
  }
}
