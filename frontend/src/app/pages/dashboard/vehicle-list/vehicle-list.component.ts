import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/auth.service';
import { Vehicle } from '../../../core/model/vehicle.model';

@Component({
  selector: 'app-vehicle-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './vehicle-list.component.html',
  styleUrls: ['./vehicle-list.component.scss'],
})
export class VehicleListComponent implements OnInit {
  vehicles: Vehicle[] = [];
  perfil: string | null = null;

  constructor(private http: HttpClient, private router: Router, private auth: AuthService) {}

  ngOnInit(): void {
    this.http.get<Vehicle[]>('http://localhost:8080/vehicle/list')
      .subscribe({
        next: (res) => this.vehicles = res,
        error: (err) => console.error('Erro ao buscar veículos', err),
      });

      this.perfil = this.auth.getPerfil();
  }

  viewDetails(vehicle: Vehicle) {
    this.router.navigate(['/veiculo', vehicle.id]);
  }

  irParaUsuarios() {
    this.router.navigate(['/usuarios']);
  }
}
