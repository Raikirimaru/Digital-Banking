import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authorizationGuard: CanActivateFn = (route, state) => {
  const authService : AuthService = inject(AuthService)
  const router : Router = inject(Router)
  if (authService.roles.includes("ADMIN")) {
    return true
  } else {
    router.navigateByUrl("/admin/not-authorized")
    return false;
  }
};
