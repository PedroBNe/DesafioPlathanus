import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule, NgFor } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/auth.service';
import { Vehicle } from '../../core/model/vehicle.model';
import { Cart } from '../../core/model/cart.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, NgFor],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  vehicle?: Vehicle;
  cart?: Cart;
  perfil: string | null = null;
  finalPrice = 0;
  tipoCompra: string = 'Pessoa Física';
  cartList: Cart[] = [];
  vehicleList: { cart: Cart; vehicle: Vehicle }[] = [];
  finalPriceTotal: number = 0;

  constructor(
    private http: HttpClient,
    private router: Router,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    this.perfil = this.auth.getPerfil();
    const username = this.auth.getUsername();

    this.http.get<Cart[]>(`http://localhost:8080/cart/user/${username}`).subscribe({
      next: (carts) => {
        this.cartList = carts;
        this.vehicleList = [];
        this.finalPriceTotal = 0;

        carts.forEach(cart => {
          this.http.get<Vehicle>(`http://localhost:8080/vehicle/detail/${cart.vehicleId}`).subscribe({
            next: (vehicle) => {
              this.vehicleList.push({ cart, vehicle });
              const preco = this.calcularPrecoFinal(vehicle);
              this.finalPriceTotal += preco;
            },
            error: () => this.router.navigate(['/'])
          });
        });
      },
      error: () => {
        alert('Carrinho vazio ou expirado.');
        this.router.navigate(['/']);
      }
    });
  }

  calcularPrecoFinal(vehicle: Vehicle): number {
    let preco = vehicle.basePrice;

    if (vehicle.color === 'PRATA') preco *= 1.01;
    else if (vehicle.color === 'PRETA') preco *= 1.02;

    if (this.perfil === 'CLIENTE' && vehicle.pcd) return preco * 0.7;
    if (this.perfil === 'CLIENTE') return preco;
    if (this.perfil === 'VENDEDOR' && vehicle.pessoaJuridica) return preco * 0.8;

    return preco;
  }

  cancelar(id: number): void {
    this.http.post(`http://localhost:8080/cart/cancel/${id}`, {}).subscribe({
      next: () => {
        this.vehicleList = this.vehicleList.filter(item => item.cart.id !== id);
        this.finalPriceTotal = this.vehicleList.reduce(
          (sum, item) => sum + this.calcularPrecoFinal(item.vehicle),
          0
        );
      },
      error: () => alert('Erro ao cancelar item do carrinho.')
    });
  }


  efetivarTodos(): void {
    const endpoint = this.perfil === 'VENDEDOR'
      ? 'http://localhost:8080/sale/physical'
      : 'http://localhost:8080/sale/online';

    const requests = this.vehicleList.map(item =>
      this.http.post(endpoint, { cartId: item.cart.id, login: this.auth.getUsername() })
    );

    Promise.all(requests.map(req => req.toPromise()))
      .then(() => {
        alert('Todas as vendas foram efetivadas com sucesso!');
        this.router.navigate(['/']);
      })
      .catch(() => {
        alert('Erro ao efetivar uma ou mais vendas. Verifique o carrinho.');
      });
  }
}
