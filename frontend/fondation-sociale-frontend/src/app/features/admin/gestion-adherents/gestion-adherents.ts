import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Adherent {
  id: number;
  prenom: string;
  nom: string;
  email: string;
  matricule: string;
  service: string;
  statut: 'actif' | 'inactif' | 'suspendu';
  dateInscription: string;
  prestationsActives: number;
}

@Component({
  selector: 'app-gestion-adherents',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gestion-adherents.html',
  styleUrls: ['./gestion-adherents.scss']
})
export class GestionAdherentsComponent implements OnInit {
  adherents: Adherent[] = [
    {
      id: 1,
      prenom: 'Jean',
      nom: 'Dupont',
      email: 'jean.dupont@ministere.gouv.fr',
      matricule: 'MIN2024001234',
      service: 'Direction Générale',
      statut: 'actif',
      dateInscription: '15 janv. 2024',
      prestationsActives: 3
    },
    {
      id: 2,
      prenom: 'Marie',
      nom: 'Martin',
      email: 'marie.martin@ministere.gouv.fr',
      matricule: 'MIN2024001235',
      service: 'Cabinet du Ministre',
      statut: 'actif',
      dateInscription: '20 janv. 2024',
      prestationsActives: 1
    },
    {
      id: 3,
      prenom: 'Pierre',
      nom: 'Durand',
      email: 'pierre.durand@ministere.gouv.fr',
      matricule: 'MIN2024001236',
      service: 'Direction des Finances',
      statut: 'inactif',
      dateInscription: '10 févr. 2024',
      prestationsActives: 0
    }
  ];

  adherentsFiltres: Adherent[] = [];
  rechercheTexte = '';
  filtreStatut = '';
  filtreService = '';

  services = ['Tous', 'Direction Générale', 'Cabinet du Ministre', 'Direction des Finances', 'Direction des Projets', 'Ressources Humaines'];
  statuts = ['Tous', 'actif', 'inactif', 'suspendu'];

  ngOnInit() {
    this.adherentsFiltres = [...this.adherents];
  }

  filtrerAdherents() {
    let resultats = [...this.adherents];

    if (this.rechercheTexte) {
      resultats = resultats.filter(a => 
        a.nom.toLowerCase().includes(this.rechercheTexte.toLowerCase()) ||
        a.prenom.toLowerCase().includes(this.rechercheTexte.toLowerCase()) ||
        a.email.toLowerCase().includes(this.rechercheTexte.toLowerCase()) ||
        a.matricule.toLowerCase().includes(this.rechercheTexte.toLowerCase())
      );
    }

    if (this.filtreStatut && this.filtreStatut !== 'Tous') {
      resultats = resultats.filter(a => a.statut === this.filtreStatut);
    }

    if (this.filtreService && this.filtreService !== 'Tous') {
      resultats = resultats.filter(a => a.service === this.filtreService);
    }

    this.adherentsFiltres = resultats;
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'actif': return 'bg-green-100 text-green-800';
      case 'inactif': return 'bg-gray-100 text-gray-800';
      case 'suspendu': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  voirProfil(adherentId: number) {
    console.log('Voir profil de l\'adhérent:', adherentId);
  }

  exporterDonnees() {
    console.log('Exporter les données des adhérents');
    alert('Export en cours...');
  }
}