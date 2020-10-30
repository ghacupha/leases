import { element, by, ElementFinder } from 'protractor';

export class ContractualLeaseRentalComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-contractual-lease-rental div table .btn-danger'));
  title = element.all(by.css('jhi-contractual-lease-rental div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class ContractualLeaseRentalUpdatePage {
  pageTitle = element(by.id('jhi-contractual-lease-rental-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  leaseContractNumberInput = element(by.id('field_leaseContractNumber'));
  rentalSequenceNumberInput = element(by.id('field_rentalSequenceNumber'));
  leaseRentalDateInput = element(by.id('field_leaseRentalDate'));
  leaseRentalAmountInput = element(by.id('field_leaseRentalAmount'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setLeaseContractNumberInput(leaseContractNumber: string): Promise<void> {
    await this.leaseContractNumberInput.sendKeys(leaseContractNumber);
  }

  async getLeaseContractNumberInput(): Promise<string> {
    return await this.leaseContractNumberInput.getAttribute('value');
  }

  async setRentalSequenceNumberInput(rentalSequenceNumber: string): Promise<void> {
    await this.rentalSequenceNumberInput.sendKeys(rentalSequenceNumber);
  }

  async getRentalSequenceNumberInput(): Promise<string> {
    return await this.rentalSequenceNumberInput.getAttribute('value');
  }

  async setLeaseRentalDateInput(leaseRentalDate: string): Promise<void> {
    await this.leaseRentalDateInput.sendKeys(leaseRentalDate);
  }

  async getLeaseRentalDateInput(): Promise<string> {
    return await this.leaseRentalDateInput.getAttribute('value');
  }

  async setLeaseRentalAmountInput(leaseRentalAmount: string): Promise<void> {
    await this.leaseRentalAmountInput.sendKeys(leaseRentalAmount);
  }

  async getLeaseRentalAmountInput(): Promise<string> {
    return await this.leaseRentalAmountInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ContractualLeaseRentalDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-contractualLeaseRental-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-contractualLeaseRental'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
