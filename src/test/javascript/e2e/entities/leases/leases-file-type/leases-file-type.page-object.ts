import { element, by, ElementFinder } from 'protractor';

export class LeasesFileTypeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-leases-file-type div table .btn-danger'));
  title = element.all(by.css('jhi-leases-file-type div h2#page-heading span')).first();
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

export class LeasesFileTypeUpdatePage {
  pageTitle = element(by.id('jhi-leases-file-type-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  leasesFileTypeNameInput = element(by.id('field_leasesFileTypeName'));
  leasesFileMediumTypeSelect = element(by.id('field_leasesFileMediumType'));
  descriptionInput = element(by.id('field_description'));
  fileTemplateInput = element(by.id('file_fileTemplate'));
  leasesfileTypeSelect = element(by.id('field_leasesfileType'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setLeasesFileTypeNameInput(leasesFileTypeName: string): Promise<void> {
    await this.leasesFileTypeNameInput.sendKeys(leasesFileTypeName);
  }

  async getLeasesFileTypeNameInput(): Promise<string> {
    return await this.leasesFileTypeNameInput.getAttribute('value');
  }

  async setLeasesFileMediumTypeSelect(leasesFileMediumType: string): Promise<void> {
    await this.leasesFileMediumTypeSelect.sendKeys(leasesFileMediumType);
  }

  async getLeasesFileMediumTypeSelect(): Promise<string> {
    return await this.leasesFileMediumTypeSelect.element(by.css('option:checked')).getText();
  }

  async leasesFileMediumTypeSelectLastOption(): Promise<void> {
    await this.leasesFileMediumTypeSelect.all(by.tagName('option')).last().click();
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setFileTemplateInput(fileTemplate: string): Promise<void> {
    await this.fileTemplateInput.sendKeys(fileTemplate);
  }

  async getFileTemplateInput(): Promise<string> {
    return await this.fileTemplateInput.getAttribute('value');
  }

  async setLeasesfileTypeSelect(leasesfileType: string): Promise<void> {
    await this.leasesfileTypeSelect.sendKeys(leasesfileType);
  }

  async getLeasesfileTypeSelect(): Promise<string> {
    return await this.leasesfileTypeSelect.element(by.css('option:checked')).getText();
  }

  async leasesfileTypeSelectLastOption(): Promise<void> {
    await this.leasesfileTypeSelect.all(by.tagName('option')).last().click();
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

export class LeasesFileTypeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-leasesFileType-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-leasesFileType'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
