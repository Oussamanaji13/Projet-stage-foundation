import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
export interface Prestation {
  id: number;
  nom: string;
  categorie: string;
  description: string;
  montantMax: string;
  duree: string;
  conditions: string;
  couleur: string;
  icone: string;
}

export interface Demande {
  id: number;
  prestationId: number;
  userId: number;
  statut: 'en_cours' | 'approuvee' | 'refusee';
  dateCreation: Date;
  dateMiseAJour: Date;
  montantDemande?: number;
  motif: string;
  documents: string[];
}

@Injectable({
  providedIn: 'root'
})
export class PrestationsService {
  private apiUrl = '/api/prestations';

  constructor(private http: HttpClient) {}

  getPrestations(): Observable<Prestation[]> {
    return this.http.get<Prestation[]>(this.apiUrl);
  }

  getPrestation(id: number): Observable<Prestation> {
    return this.http.get<Prestation>(`${this.apiUrl}/${id}`);
  }

  creerDemande(demande: Partial<Demande>): Observable<Demande> {
    return this.http.post<Demande>('/api/demandes', demande);
  }

  getDemandesUtilisateur(userId: number): Observable<Demande[]> {
    return this.http.get<Demande[]>('/api/demandes');
  }
}

