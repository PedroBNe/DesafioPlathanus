import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../core/auth.service';

export const vendedorGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const perfil = auth.getPerfil();

  if (perfil !== 'VENDEDOR') {
    router.navigate(['/']);
    return false;
  }

  return true;
};
