import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface Partenaire {
  id: number;
  nom: string;
  secteur: string;
  telephone: string;
  email: string;
  adresse: string;
  description: string;
  icone: string;
  couleur: string;
}

@Component({
  selector: 'app-annuaire',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './annuaire.html',
  styleUrls: ['./annuaire.scss']
})
export class AnnuaireComponent implements OnInit {
  partenaires: Partenaire[] = [
    {
      id: 1,
      nom: 'Banque Nationale',
      secteur: 'Banque',
      telephone: '01 23 45 67 89',
      email: 'contact@banque-nationale.fr',
      adresse: '123 Avenue des Champs\n75008 Paris',
      description: 'Prêts immobiliers à taux préférentiels pour les fonctionnaires',
      icone: 'fas fa-university',
      couleur: 'bg-blue-100 text-blue-600'
    },
    {
      id: 2,
      nom: 'Assurance Plus',
      secteur: 'Assurance',
      telephone: '01 98 76 54 32',
      email: 'info@assurance-plus.fr',
      adresse: '456 Rue de la Paix\n69000 Lyon',
      description: 'Assurances auto, habitation et santé avec remises spéciales',
      icone: 'fas fa-shield-alt',
      couleur: 'bg-green-100 text-green-600'
    },
    {
      id: 3,
      nom: 'Immobilier Pro',
      secteur: 'Immobilier',
      telephone: '04 56 78 90 12',
      email: 'contact@immobilier-pro.fr',
      adresse: '789 Boulevard Sud\n13000 Marseille',
      description: 'Accompagnement dans vos projets immobiliers',
      icone: 'fas fa-home',
      couleur: 'bg-purple-100 text-purple-600'
    },
    {
      id: 4,
      nom: 'Formation Expert',
      secteur: 'Formation',
      telephone: '02 34 56 78 90',
      email: 'info@formation-expert.fr',
      adresse: '321 Rue des Écoles\n44000 Nantes',
      description: 'Formations professionnelles et certifiantes',
      icone: 'fas fa-graduation-cap',
      couleur: 'bg-orange-100 text-orange-600'
    },
    {
      id: 5,
      nom: 'Santé Bien-être',
      secteur: 'Santé',
      telephone: '05 67 89 01 23',
      email: 'contact@sante-bienetre.fr',
      adresse: '654 Avenue Santé\n31000 Toulouse',
      description: 'Services de santé et bien-être pour les fonctionnaires',
      icone: 'fas fa-heartbeat',
      couleur: 'bg-red-100 text-red-600'
    },
    {
      id: 6,
      nom: 'Auto Services',
      secteur: 'Automobile',
      telephone: '03 45 67 89 01',
      email: 'info@auto-services.fr',
      adresse: '987 Route Nationale\n67000 Strasbourg',
      description: 'Vente et entretien automobile avec tarifs préférentiels',
      icone: 'fas fa-car',
      couleur: 'bg-indigo-100 text-indigo-600'
    }
  ];

  partenairesFiltres: Partenaire[] = [];
  rechercheTexte = '';
  secteurSelectionne = '';
  secteurs = ['Tous les secteurs', 'Banques', 'Assurances', 'Immobilier', 'Formation', 'Santé', 'Automobile'];

  ngOnInit() {
    this.partenairesFiltres = [...this.partenaires];
  }

  filtrerPartenaires() {
    let resultats = [...this.partenaires];

    // Filtrage par texte
    if (this.rechercheTexte) {
      resultats = resultats.filter(p => 
        p.nom.toLowerCase().includes(this.rechercheTexte.toLowerCase()) ||
        p.description.toLowerCase().includes(this.rechercheTexte.toLowerCase())
      );
    }

    // Filtrage par secteur
    if (this.secteurSelectionne && this.secteurSelectionne !== 'Tous les secteurs') {
      resultats = resultats.filter(p => 
        p.secteur.toLowerCase() === this.secteurSelectionne.toLowerCase().slice(0, -1)
      );
    }

    this.partenairesFiltres = resultats;
  }
}
