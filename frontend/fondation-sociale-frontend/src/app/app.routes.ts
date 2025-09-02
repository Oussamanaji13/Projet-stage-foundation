import { Routes } from '@angular/router';
import { PublicLayout } from './core/layouts/public-layout/public-layout';
import { UserLayoutComponent } from './core/layouts/user-layout/user-layout';
import { AdminLayout } from './core/layouts/admin-layout/admin-layout';
import { authGuard } from './core/guards/auth-guard';
import { adminGuard } from './core/guards/admin-guard';

export const routes: Routes = [
  // Routes publiques
  {
    path: '',
    component: PublicLayout,
    children: [
      {
        path: '',
        loadComponent: () => import('./features/public/accueil/accueil').then(m => m.Accueil)
      },
      {
        path: 'actualites',
        loadComponent: () => import('./features/public/actualites/actualites').then(m => m.ActualitesComponent)
      },
      {
        path: 'ministere',
        loadComponent: () => import('./features/public/ministere/ministere').then(m => m.MinistereComponent)
      },
      {
        path: 'annuaire',
        loadComponent: () => import('./features/public/annuaire/annuaire').then(m => m.AnnuaireComponent)
      },
      {
        path: 'contact',
        loadComponent: () => import('./features/public/contact/contact').then(m => m.ContactComponent)
      }
    ]
  },

  // Routes d'authentification
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register').then(m => m.Register)
  },

  // Routes utilisateur (protégées)
  {
    path: 'user',
    component: UserLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/user/dashboard/dashboard').then(m => m.Dashboard)
      },
      {
        path: 'profil',
        loadComponent: () => import('./features/user/profil/profil').then(m => m.ProfilComponent)
      },
      {
        path: 'prestations',
        loadComponent: () => import('./features/user/prestations/prestations').then(m => m.Prestations)
      },
      {
        path: 'demandes',
        loadComponent: () => import('./features/user/demandes/demandes').then(m => m.DemandesComponent)
      },
      {
        path: 'avis',
        loadComponent: () => import('./features/user/avis/avis').then(m => m.AvisComponent)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },

  // Routes admin (protégées)
  {
    path: 'admin',
    component: AdminLayout,
    canActivate: [authGuard, adminGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/admin/dashboard/dashboard').then(m => m.DashboardComponent)
      },
      {
        path: 'adherents',
        loadComponent: () => import('./features/admin/gestion-adherents/gestion-adherents').then(m => m.GestionAdherentsComponent)
      },
      {
        path: 'evenements',
        loadComponent: () => import('./features/admin/gestion-evenements/gestion-evenements').then(m => m.GestionEvenementsComponent)
      },
      {
        path: 'avis',
        loadComponent: () => import('./features/admin/gestion-avis/gestion-avis').then(m => m.GestionAvis)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },

  // Route par défaut
  {
    path: '**',
    redirectTo: ''
  }
];
