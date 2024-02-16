import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authenticationGuard : CanActivateFn = (route, state) => {
  const authService : AuthService = inject(AuthService)
  const router : Router = inject(Router)
  if (authService.isAuthenticated === true) {
    return true
  } else {
    router.navigateByUrl("/signin")
    return false
  }
};
