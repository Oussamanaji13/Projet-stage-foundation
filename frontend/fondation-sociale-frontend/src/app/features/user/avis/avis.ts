import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Avis {
  id: number;
  prestation: string;
  note: number;
  commentaire: string;
  date: string;
  statut: 'publié' | 'en_attente' | 'modéré';
}

@Component({
  selector: 'app-avis',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './avis.html',
  styleUrls: ['./avis.scss']
})
export class AvisComponent implements OnInit {
  mesAvis: Avis[] = [
    {
      id: 1,
      prestation: 'Aide au logement',
      note: 5,
      commentaire: 'Service excellent, très rapide et efficace. L\'équipe est très professionnelle.',
      date: '15 nov. 2024',
      statut: 'publié'
    },
    {
      id: 2,
      prestation: 'Bourse de formation',
      note: 4,
      commentaire: 'Très bon accompagnement pour ma formation. Quelques délais mais résultat satisfaisant.',
      date: '2 oct. 2024',
      statut: 'publié'
    }
  ];

  nouvelAvis = {
    prestation: '',
    note: 0,
    commentaire: ''
  };

  prestationsDisponibles = [
    'Aide au logement',
    'Bourse de formation',
    'Aide garde d\'enfants',
    'Complémentaire santé',
    'Chèques vacances',
    'Équipement informatique'
  ];

  ngOnInit() {
    // Initialisation
  }

  ajouterAvis() {
    if (!this.nouvelAvis.prestation || !this.nouvelAvis.note || !this.nouvelAvis.commentaire) {
      alert('Veuillez remplir tous les champs');
      return;
    }

    const avis: Avis = {
      id: Date.now(),
      prestation: this.nouvelAvis.prestation,
      note: this.nouvelAvis.note,
      commentaire: this.nouvelAvis.commentaire,
      date: new Date().toLocaleDateString('fr-FR'),
      statut: 'en_attente'
    };

    this.mesAvis.unshift(avis);
    this.resetForm();
    alert('Votre avis a été ajouté avec succès !');
  }

  private resetForm() {
    this.nouvelAvis = {
      prestation: '',
      note: 0,
      commentaire: ''
    };
  }

  getEtoiles(note: number): string[] {
    const etoiles = [];
    for (let i = 1; i <= 5; i++) {
      etoiles.push(i <= note ? 'fas fa-star text-yellow-500' : 'far fa-star text-gray-300');
    }
    return etoiles;
  }

  getStatutBadge(statut: string): string {
    switch (statut) {
      case 'publié': return 'bg-green-100 text-green-800';
      case 'en_attente': return 'bg-yellow-100 text-yellow-800';
      case 'modéré': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  getStatutLabel(statut: string): string {
    switch (statut) {
      case 'publié': return 'Publié';
      case 'en_attente': return 'En attente';
      case 'modéré': return 'Modéré';
      default: return 'Inconnu';
    }
  }
}
