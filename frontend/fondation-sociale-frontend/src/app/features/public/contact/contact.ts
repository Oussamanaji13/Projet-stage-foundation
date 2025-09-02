import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contact.html',
  styleUrls: ['./contact.scss']
})
export class ContactComponent {
  contact = {
    prenom: '',
    nom: '',
    email: '',
    telephone: '',
    sujet: '',
    message: ''
  };

  sujets = [
    { value: '', label: 'Sélectionnez un sujet' },
    { value: 'adhesion', label: 'Demande d\'adhésion' },
    { value: 'prestation', label: 'Question sur une prestation' },
    { value: 'evenement', label: 'Inscription à un événement' },
    { value: 'reclamation', label: 'Réclamation' },
    { value: 'autre', label: 'Autre' }
  ];

  onSubmit() {
    console.log('Formulaire de contact soumis:', this.contact);
    // Ici, vous ajouteriez la logique pour envoyer le message
    alert('Votre message a été envoyé avec succès !');
    this.resetForm();
  }

  private resetForm() {
    this.contact = {
      prenom: '',
      nom: '',
      email: '',
      telephone: '',
      sujet: '',
      message: ''
    };
  }
}
