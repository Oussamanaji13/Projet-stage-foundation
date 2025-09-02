import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="bg-white rounded-2xl card-shadow overflow-hidden" 
         [class]="additionalClasses">
      <div *ngIf="header" class="p-6 border-b border-gray-200">
        <h3 class="text-xl font-semibold text-gray-900">{{ header }}</h3>
      </div>
      <div class="p-6">
        <ng-content></ng-content>
      </div>
      <div *ngIf="footer" class="px-6 py-4 bg-gray-50 border-t border-gray-200">
        <ng-content select="[slot=footer]"></ng-content>
      </div>
    </div>
  `,
  styleUrls: ['./card.scss']
})
export class CardComponent {
  @Input() header?: string;
  @Input() footer?: boolean = false;
  @Input() additionalClasses?: string = '';
}
