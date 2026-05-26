import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject, runInInjectionContext, EnvironmentInjector } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { catchError, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const injector = inject(EnvironmentInjector);

  // ✅ Don't attach expired token to auth endpoints
  const isAuthUrl = req.url.includes('/auth/');
  const authReq = token && !isAuthUrl
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !req.url.includes('/auth/refresh')) {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
          localStorage.clear();
          runInInjectionContext(injector, () => inject(Router).navigate(['/login']));
          return throwError(() => error);
        }

        return runInInjectionContext(injector, () => {
          const http = inject(HttpClient);
          const router = inject(Router);

          // ✅ Send refresh without any Authorization header
          const refreshReq = new (req.constructor as any)(
            'POST',
            'http://localhost:8080/auth/refresh',
            { refreshToken }
          );

          return http.post<string>(
            'http://localhost:8080/auth/refresh',
            { refreshToken }
          ).pipe(
            switchMap((newToken: string) => {
              localStorage.setItem('token', newToken);
              const retryReq = req.clone({
                setHeaders: { Authorization: `Bearer ${newToken}` }
              });
              return next(retryReq);
            }),
            catchError((refreshError) => {
              localStorage.clear();
              router.navigate(['/login']);
              return throwError(() => refreshError);
            })
          );
        });
      }
      return throwError(() => error);
    })
  );
};