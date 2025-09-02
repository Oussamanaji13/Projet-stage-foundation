import { Component } from '@angular/core';
import { Header } from '../../../shared/components/header/header';
import { RouterModule, RouterOutlet } from '@angular/router';
import { Footer } from '../../../shared/components/footer/footer';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-public-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet, Header, Footer],
  templateUrl: './public-layout.html',
  styleUrl: './public-layout.scss'
})
export class PublicLayout {

}
