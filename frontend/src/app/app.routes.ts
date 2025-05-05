import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { authGuard } from './core/auth.guard';
import { vendedorGuard } from './auth/vendedor.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/dashboard/vehicle-list/vehicle-list.component').then(m => m.VehicleListComponent)
  },
  {
    path: 'veiculo/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/dashboard/vehicle-detail/vehicle-detail.component').then(m => m.VehicleDetailComponent)
  },
  {
    path: 'carrinho',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/cart/cart.component').then(m => m.CartComponent)
  },
  {
    path: 'usuarios',
    canActivate: [authGuard, vendedorGuard],
    loadComponent: () => import('./pages/users/user-list.component').then(m => m.UserListComponent)
  },
  { path: '**', redirectTo: '' }
];
