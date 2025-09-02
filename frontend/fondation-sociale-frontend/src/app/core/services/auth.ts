import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap, map } from 'rxjs/operators';

interface User {
  id: number;
  prenom: string;
  nom: string;
  email: string;
  matricule: string;
  service: string;
  role: 'user' | 'admin';
}
@Injectable({
  providedIn: 'root'
})
export class Auth {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
  private http: HttpClient,
  private router: Router
  ) {
    // Vérifier si un utilisateur est déjà connecté
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>('/api/auth/login', { email, password }).pipe(
      tap(response => {
        if (response.success) {
          const user = response.user;
          localStorage.setItem('currentUser', JSON.stringify(user));
          localStorage.setItem('token', response.token);
          this.currentUserSubject.next(user);
        }
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post<any>('/api/auth/register', userData);
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/']);
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.getCurrentUser();
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'admin';
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Simulation des appels API
  private simulateLogin(email: string, password: string): Observable<any> {
    // Utilisateur de test
    const testUser: User = {
      id: 1,
      prenom: 'Jean',
      nom: 'Dupont',
      email: email,
      matricule: 'MIN2024001234',
      service: 'Direction Générale',
      role: email.includes('admin') ? 'admin' : 'user'
    };

    // Simulation d'un délai réseau
    return of({
      success: true,
      user: testUser,
      token: 'fake-jwt-token-' + Date.now()
    });
  }

  private simulateRegister(userData: any): Observable<any> {
    // Simulation d'inscription réussie
    return of({
      success: true,
      message: 'Inscription réussie'
    });
  }
}
