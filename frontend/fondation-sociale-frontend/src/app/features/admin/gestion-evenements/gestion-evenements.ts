import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

type TypeEvenement = 'assemblée' | 'formation' | 'forum' | 'social';  // TYPE DÉFINI

interface Evenement {
  id: number;
  titre: string;
  description: string;
  date: string;
  heure: string;
  lieu: string;
  type: TypeEvenement;
  statut: 'planifié' | 'en_cours' | 'terminé' | 'annulé';
  participants: number;
  capaciteMax: number;
}

@Component({
  selector: 'app-gestion-evenements',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gestion-evenements.html',
  styleUrls: ['./gestion-evenements.scss']
})
export class GestionEvenementsComponent implements OnInit {
  evenements: Evenement[] = [
    {
      id: 1,
      titre: 'Assemblée Générale 2025',
      description: 'Assemblée générale annuelle de la fondation',
      date: '2025-01-20',
      heure: '14:00',
      lieu: 'Amphithéâtre du Ministère',
      type: 'assemblée',
      statut: 'planifié',
      participants: 145,
      capaciteMax: 200
    }
  ];

  nouvelEvenement = {
    titre: '',
    description: '',
    date: '',
    heure: '',
    lieu: '',
    type: '' as TypeEvenement | '',  // TYPAGE CORRIGÉ
    capaciteMax: 50
  };

  types: TypeEvenement[] = ['assemblée', 'formation', 'forum', 'social'];
  afficherFormulaire = false;

  ngOnInit() {}

  creerEvenement() {
    if (!this.nouvelEvenement.titre || !this.nouvelEvenement.date || !this.nouvelEvenement.type) {
      alert('Veuillez remplir les champs obligatoires');
      return;
    }

    const evenement: Evenement = {
      id: Date.now(),
      titre: this.nouvelEvenement.titre,
      description: this.nouvelEvenement.description,
      date: this.nouvelEvenement.date,
      heure: this.nouvelEvenement.heure,
      lieu: this.nouvelEvenement.lieu,
      type: this.nouvelEvenement.type as TypeEvenement,  // CAST SÉCURISÉ
      capaciteMax: this.nouvelEvenement.capaciteMax,
      statut: 'planifié',
      participants: 0
    };

    this.evenements.unshift(evenement);
    this.resetFormulaire();
    this.afficherFormulaire = false;
    alert('Événement créé avec succès !');
  }

  private resetFormulaire() {
    this.nouvelEvenement = {
      titre: '',
      description: '',
      date: '',
      heure: '',
      lieu: '',
      type: '',
      capaciteMax: 50
    };
  }

  getStatutClass(statut: string): string {
    switch (statut) {
      case 'planifié': return 'bg-blue-100 text-blue-800';
      case 'en_cours': return 'bg-green-100 text-green-800';
      case 'terminé': return 'bg-gray-100 text-gray-800';
      case 'annulé': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getTypeClass(type: string): string {
    switch (type) {
      case 'assemblée': return 'bg-purple-100 text-purple-800';
      case 'formation': return 'bg-green-100 text-green-800';
      case 'forum': return 'bg-blue-100 text-blue-800';
      case 'social': return 'bg-yellow-100 text-yellow-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  modifierEvenement(id: number) {
    console.log('Modifier événement:', id);
  }

  supprimerEvenement(id: number) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet événement ?')) {
      this.evenements = this.evenements.filter(e => e.id !== id);
    }
  }
}