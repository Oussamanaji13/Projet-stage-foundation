// core/services/notifications.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
export interface Notification {
  id: number;
  titre: string;
  message: string;
  type: 'info' | 'success' | 'warning' | 'error';
  lu: boolean;
  dateCreation: Date;
}

@Injectable({
  providedIn: 'root'
})
export class notifications {
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  constructor() {
    // Initialiser avec quelques notifications de test
    this.initializeTestNotifications();
  }

  getNotifications(): Observable<Notification[]> {
    return this.notifications$;
  }

  getNotificationsNonLues(): Observable<Notification[]> {
    return this.notifications$.pipe(
      map(notifications => notifications.filter(n => !n.lu))
    );
  }

  marquerCommeLue(notificationId: number): void {
    const notifications = this.notificationsSubject.value;
    const notification = notifications.find(n => n.id === notificationId);
    if (notification) {
      notification.lu = true;
      this.notificationsSubject.next([...notifications]);
    }
  }

  ajouterNotification(notification: Omit<Notification, 'id' | 'lu' | 'dateCreation'>): void {
    const nouvelles = this.notificationsSubject.value;
    const nouvelleNotification: Notification = {
      ...notification,
      id: Date.now(),
      lu: false,
      dateCreation: new Date()
    };
    
    nouvelles.unshift(nouvelleNotification);
    this.notificationsSubject.next(nouvelles);
  }

  private initializeTestNotifications(): void {
    const notifications: Notification[] = [
      {
        id: 1,
        titre: 'Demande approuvée',
        message: 'Votre demande de bourse formation a été approuvée',
        type: 'success',
        lu: false,
        dateCreation: new Date(Date.now() - 2 * 60 * 60 * 1000) // Il y a 2 heures
      },
      {
        id: 2,
        titre: 'Nouvel événement',
        message: 'Assemblée générale le 20 janvier 2025',
        type: 'info',
        lu: false,
        dateCreation: new Date(Date.now() - 24 * 60 * 60 * 1000) // Il y a 1 jour
      },
      {
        id: 3,
        titre: 'Document manquant',
        message: 'Merci de compléter votre dossier d\'aide au logement',
        type: 'warning',
        lu: false,
        dateCreation: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000) // Il y a 3 jours
      }
    ];

    this.notificationsSubject.next(notifications);
  }
}