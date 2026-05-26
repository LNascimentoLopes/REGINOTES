import {
  HttpInterceptorFn,
  HttpErrorResponse,
  HttpBackend,
  HttpClient,
} from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError, BehaviorSubject, filter, take } from 'rxjs';

// Variáveis globais fora da função para manter o estado entre requisições
let isRefreshing = false;
let refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  // ✅ Injetamos o HttpBackend para criar um HttpClient que IGNORA os interceptores
  const httpBackend = inject(HttpBackend);
  const http = new HttpClient(httpBackend);

  const token = localStorage.getItem('token');
  const isAuthUrl = req.url.includes('/auth/');

  const authReq =
    token && !isAuthUrl ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Se deu 401 e não é a própria rota de refresh falhando
      if (error.status === 401 && !req.url.includes('/auth/refresh')) {
        // Se já tem um refresh acontecendo, coloca na fila
        if (isRefreshing) {
          return refreshTokenSubject.pipe(
            filter((newToken) => newToken !== null),
            take(1),
            switchMap((newToken) => {
              return next(req.clone({ setHeaders: { Authorization: `Bearer ${newToken}` } }));
            }),
          );
        }

        // Se NÃO tem um refresh acontecendo, nós iniciamos um
        isRefreshing = true;
        refreshTokenSubject.next(null); // Reseta o subject

        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
          isRefreshing = false;
          localStorage.clear();
          router.navigate(['/login']);
          return throwError(() => error);
        }

        // Faz o POST usando o HttpClient "puro"
        return http.post<any>('http://localhost:8080/auth/refresh', { refreshToken }).pipe(
          switchMap((response) => {
            isRefreshing = false;

            // Pegando as chaves com os nomes EXATOS que o back-end mandou
            const newToken = response.Token; // T maiúsculo!
            const newRefreshToken = response.refreshToken;

            // Atualizando os dois tokens no Local Storage
            localStorage.setItem('token', newToken);
            if (newRefreshToken) {
              localStorage.setItem('refreshToken', newRefreshToken);
            }

            // Avisa a fila de espera qual é o novo token de acesso
            refreshTokenSubject.next(newToken);

            // Clona e refaz a requisição que tinha dado erro
            const retryReq = req.clone({
              setHeaders: { Authorization: `Bearer ${newToken}` },
            });
            return next(retryReq);
          }),
        );
      }

      // Se não for 401, só repassa o erro pra frente
      return throwError(() => error);
    }),
  );
};
