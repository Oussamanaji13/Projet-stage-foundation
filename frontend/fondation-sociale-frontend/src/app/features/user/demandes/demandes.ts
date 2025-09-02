import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

interface EtapeFormulaire {
  numero: number;
  titre: string;
  active: boolean;
  completee: boolean;
}

interface Prestation {
  id: number;
  nom: string;
  description: string;
  icone: string;
  couleur: string;
}

interface DemandeExistante {
  id: string;
  titre: string;
  date: string;
  statut: string;
  couleurStatut: string;
  couleurFond: string;
  progression: number;
  etapes: string[];
}

@Component({
  selector: 'app-demandes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './demandes.html',
  styleUrls: ['./demandes.scss']
})
export class DemandesComponent implements OnInit {
  etapesFormulaire: EtapeFormulaire[] = [
    { numero: 1, titre: 'Type de prestation', active: true, completee: false },
    { numero: 2, titre: 'Informations', active: false, completee: false },
    { numero: 3, titre: 'Documents', active: false, completee: false },
    { numero: 4, titre: 'Validation', active: false, completee: false }
  ];

  prestationsDisponibles: Prestation[] = [
    {
      id: 1,
      nom: 'Aide au logement',
      description: 'Jusqu\'à 800€/mois',
      icone: 'fas fa-home',
      couleur: 'bg-blue-100 text-blue-600'
    },
    {
      id: 2,
      nom: 'Bourse de formation',
      description: 'Jusqu\'à 3 000€',
      icone: 'fas fa-graduation-cap',
      couleur: 'bg-green-100 text-green-600'
    },
    {
      id: 3,
      nom: 'Aide garde d\'enfants',
      description: 'Jusqu\'à 400€/mois',
      icone: 'fas fa-baby',
      couleur: 'bg-purple-100 text-purple-600'
    },
    {
      id: 4,
      nom: 'Complémentaire santé',
      description: 'Jusqu\'à 30% de réduction',
      icone: 'fas fa-heartbeat',
      couleur: 'bg-red-100 text-red-600'
    }
  ];

  demandesExistantes: DemandeExistante[] = [
    {
      id: 'AL-2024-001247',
      titre: 'Aide au logement',
      date: '10 déc. 2024',
      statut: 'En cours',
      couleurStatut: 'bg-yellow-100 text-yellow-800',
      couleurFond: 'bg-yellow-50 border-yellow-400',
      progression: 70,
      etapes: ['Dépôt', 'Vérification', 'Instruction', 'Décision']
    },
    {
      id: 'BF-2024-000892',
      titre: 'Bourse de formation',
      date: '5 déc. 2024',
      statut: 'Approuvée',
      couleurStatut: 'bg-green-100 text-green-800',
      couleurFond: 'bg-green-50 border-green-400',
      progression: 100,
      etapes: ['Dépôt', 'Vérification', 'Instruction', 'Décision']
    }
  ];

  nouvelleDemande = {
    prestationId: '',
    motif: '',
    montantDemande: '',
    dateDebut: ''
  };
Math: any;

  ngOnInit() {
    // Initialisation
  }

  selectionnerPrestation(prestationId: number) {
    this.nouvelleDemande.prestationId = prestationId.toString();
  }

  continuerFormulaire() {
    if (!this.nouvelleDemande.prestationId) {
      alert('Veuillez sélectionner une prestation');
      return;
    }
    
    console.log('Continuer vers l\'étape suivante:', this.nouvelleDemande);
    // Logique pour passer à l'étape suivante
  }

  voirDetails(demandeId: string) {
    console.log('Voir détails de la demande:', demandeId);
  }
}
