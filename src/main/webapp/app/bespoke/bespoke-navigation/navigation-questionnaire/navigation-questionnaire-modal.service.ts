import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NavigationQuestionnaireComponent } from 'app/bespoke/bespoke-navigation/navigation-questionnaire/navigation-questionnaire.component';

/**
 * This questionnaire opens or closes the navigation questionnaire in a modal when prompted
 */
@Injectable({
  providedIn: 'root',
})
export class NavigationQuestionnaireModalService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef: NgbModalRef = this.modalService.open(NavigationQuestionnaireComponent);
    modalRef.result.finally(() => (this.isOpen = false));
  }

  close(): void {
    if (this.modalService.hasOpenModals()) {
      this.modalService.dismissAll('Questionnaire-navigation client requested closure...');
    }
  }
}
