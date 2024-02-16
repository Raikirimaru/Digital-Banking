import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AppHttpInterceptor implements HttpInterceptor {

  constructor(private authService : AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    console.log(request.url);
    if (!request.url.includes("/auth/signin")) {
      let req = request.clone({
        headers : request.headers.set("Authorization", `Bearer ${this.authService.access_token}`)
      })

      return next.handle(req)
    } else {
      return next.handle(request);
    }
  }
}
