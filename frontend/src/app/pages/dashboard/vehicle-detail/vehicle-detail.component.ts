import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Vehicle } from '../../../core/model/vehicle.model';
import { AuthService } from '../../../core/auth.service';

@Component({
  selector: 'app-vehicle-detail',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './vehicle-detail.component.html',
  styleUrls: ['./vehicle-detail.component.scss'],
})
export class VehicleDetailComponent implements OnInit {
  vehicle?: Vehicle;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.http.get<Vehicle>(`http://localhost:8080/vehicle/detail/${id}`)
      .subscribe({
        next: (v) => this.vehicle = v,
        error: () => this.router.navigate(['/'])
      });
  }

  adicionarAoCarrinho() {
    if (!this.vehicle) return;

    const token = this.auth.getToken();
    this.http.post('http://localhost:8080/cart/add', {
      vehicleId: this.vehicle.id,
      username: this.auth.getUsername()
    }, {
      params: token ? { token } : {}
    }).subscribe({
      next: () => this.router.navigate(['/carrinho']),
      error: () => alert('Erro ao adicionar ao carrinho.')
    });
  }

  voltar() {
    this.router.navigate(['/']);
  }
}
