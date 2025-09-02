import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';

import { Auth } from '../services/auth';
import { authGuard } from './auth-guard';

export const adminGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  if (auth.isAuthenticated() && auth.isAdmin()) {
    return true;
  }

  // Rediriger vers le dashboard utilisateur si connecté mais pas admin
  if (auth.isAuthenticated()) {
    router.navigate(['/user/dashboard']);
  } else {
    router.navigate(['/login']);
  }
  
  return false;
};