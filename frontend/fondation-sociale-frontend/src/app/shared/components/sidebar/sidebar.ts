import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Auth } from '../../../core/services/auth';


interface MenuItem {
  icon: string;
  label: string;
  route: string;
  badge?: number;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class SidebarComponent {
 @Input() userType: 'user' | 'admin' = 'user';

  userMenuItems: MenuItem[] = [
    { icon: 'fas fa-home', label: 'Tableau de bord', route: '/user/dashboard' },
    { icon: 'fas fa-heart', label: 'Prestations', route: '/user/prestations' },
    { icon: 'fas fa-file-alt', label: 'Mes demandes', route: '/user/demandes', badge: 3 },
    { icon: 'fas fa-user', label: 'Mon profil', route: '/user/profil' },
    { icon: 'fas fa-star', label: 'Mes avis', route: '/user/avis' }
  ];

  adminMenuItems: MenuItem[] = [
    { icon: 'fas fa-tachometer-alt', label: 'Dashboard Admin', route: '/admin/dashboard' },
    { icon: 'fas fa-users', label: 'Gestion Adhérents', route: '/admin/adherents' },
    { icon: 'fas fa-calendar', label: 'Gestion Événements', route: '/admin/evenements' },
    { icon: 'fas fa-star', label: 'Gestion Avis', route: '/admin/avis' }
  ];

  constructor(
    private router: Router,
    private auth: Auth
  ) {}

  get menuItems(): MenuItem[] {
    return this.userType === 'admin' ? this.adminMenuItems : this.userMenuItems;
  }

  logout() {
    this.auth.logout();
  }
}

