import { element, by, ElementFinder } from 'protractor';

export class LeaseDetailsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-lease-details div table .btn-danger'));
  title = element.all(by.css('jhi-lease-details div h2#page-heading span')).first();
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

export class LeaseDetailsUpdatePage {
  pageTitle = element(by.id('jhi-lease-details-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  leaseContractNumberInput = element(by.id('field_leaseContractNumber'));
  incrementalBorrowingRateInput = element(by.id('field_incrementalBorrowingRate'));
  commencementDateInput = element(by.id('field_commencementDate'));
  leasePrepaymentsInput = element(by.id('field_leasePrepayments'));
  initialDirectCostsInput = element(by.id('field_initialDirectCosts'));
  demolitionCostsInput = element(by.id('field_demolitionCosts'));
  assetAccountNumberInput = element(by.id('field_assetAccountNumber'));
  liabilityAccountNumberInput = element(by.id('field_liabilityAccountNumber'));
  depreciationAccountNumberInput = element(by.id('field_depreciationAccountNumber'));
  interestAccountNumberInput = element(by.id('field_interestAccountNumber'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setLeaseContractNumberInput(leaseContractNumber: string): Promise<void> {
    await this.leaseContractNumberInput.sendKeys(leaseContractNumber);
  }

  async getLeaseContractNumberInput(): Promise<string> {
    return await this.leaseContractNumberInput.getAttribute('value');
  }

  async setIncrementalBorrowingRateInput(incrementalBorrowingRate: string): Promise<void> {
    await this.incrementalBorrowingRateInput.sendKeys(incrementalBorrowingRate);
  }

  async getIncrementalBorrowingRateInput(): Promise<string> {
    return await this.incrementalBorrowingRateInput.getAttribute('value');
  }

  async setCommencementDateInput(commencementDate: string): Promise<void> {
    await this.commencementDateInput.sendKeys(commencementDate);
  }

  async getCommencementDateInput(): Promise<string> {
    return await this.commencementDateInput.getAttribute('value');
  }

  async setLeasePrepaymentsInput(leasePrepayments: string): Promise<void> {
    await this.leasePrepaymentsInput.sendKeys(leasePrepayments);
  }

  async getLeasePrepaymentsInput(): Promise<string> {
    return await this.leasePrepaymentsInput.getAttribute('value');
  }

  async setInitialDirectCostsInput(initialDirectCosts: string): Promise<void> {
    await this.initialDirectCostsInput.sendKeys(initialDirectCosts);
  }

  async getInitialDirectCostsInput(): Promise<string> {
    return await this.initialDirectCostsInput.getAttribute('value');
  }

  async setDemolitionCostsInput(demolitionCosts: string): Promise<void> {
    await this.demolitionCostsInput.sendKeys(demolitionCosts);
  }

  async getDemolitionCostsInput(): Promise<string> {
    return await this.demolitionCostsInput.getAttribute('value');
  }

  async setAssetAccountNumberInput(assetAccountNumber: string): Promise<void> {
    await this.assetAccountNumberInput.sendKeys(assetAccountNumber);
  }

  async getAssetAccountNumberInput(): Promise<string> {
    return await this.assetAccountNumberInput.getAttribute('value');
  }

  async setLiabilityAccountNumberInput(liabilityAccountNumber: string): Promise<void> {
    await this.liabilityAccountNumberInput.sendKeys(liabilityAccountNumber);
  }

  async getLiabilityAccountNumberInput(): Promise<string> {
    return await this.liabilityAccountNumberInput.getAttribute('value');
  }

  async setDepreciationAccountNumberInput(depreciationAccountNumber: string): Promise<void> {
    await this.depreciationAccountNumberInput.sendKeys(depreciationAccountNumber);
  }

  async getDepreciationAccountNumberInput(): Promise<string> {
    return await this.depreciationAccountNumberInput.getAttribute('value');
  }

  async setInterestAccountNumberInput(interestAccountNumber: string): Promise<void> {
    await this.interestAccountNumberInput.sendKeys(interestAccountNumber);
  }

  async getInterestAccountNumberInput(): Promise<string> {
    return await this.interestAccountNumberInput.getAttribute('value');
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

export class LeaseDetailsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leaseDetails-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leaseDetails'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
