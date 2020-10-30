import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LeasesFileUploadComponentsPage, LeasesFileUploadDeleteDialog, LeasesFileUploadUpdatePage } from './leases-file-upload.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeasesFileUpload e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leasesFileUploadComponentsPage: LeasesFileUploadComponentsPage;
  let leasesFileUploadUpdatePage: LeasesFileUploadUpdatePage;
  let leasesFileUploadDeleteDialog: LeasesFileUploadDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeasesFileUploads', async () => {
    await navBarPage.goToEntity('leases-file-upload');
    leasesFileUploadComponentsPage = new LeasesFileUploadComponentsPage();
    await browser.wait(ec.visibilityOf(leasesFileUploadComponentsPage.title), 5000);
    expect(await leasesFileUploadComponentsPage.getTitle()).to.eq('Leases File Uploads');
    await browser.wait(
      ec.or(ec.visibilityOf(leasesFileUploadComponentsPage.entities), ec.visibilityOf(leasesFileUploadComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeasesFileUpload page', async () => {
    await leasesFileUploadComponentsPage.clickOnCreateButton();
    leasesFileUploadUpdatePage = new LeasesFileUploadUpdatePage();
    expect(await leasesFileUploadUpdatePage.getPageTitle()).to.eq('Create or edit a Leases File Upload');
    await leasesFileUploadUpdatePage.cancel();
  });

  it('should create and save LeasesFileUploads', async () => {
    const nbButtonsBeforeCreate = await leasesFileUploadComponentsPage.countDeleteButtons();

    await leasesFileUploadComponentsPage.clickOnCreateButton();

    await promise.all([
      leasesFileUploadUpdatePage.setDescriptionInput('description'),
      leasesFileUploadUpdatePage.setFileNameInput('fileName'),
      leasesFileUploadUpdatePage.setPeriodFromInput('2000-12-31'),
      leasesFileUploadUpdatePage.setPeriodToInput('2000-12-31'),
      leasesFileUploadUpdatePage.setLeasesFileTypeIdInput('5'),
      leasesFileUploadUpdatePage.setDataFileInput(absolutePath),
      leasesFileUploadUpdatePage.setUploadTokenInput('uploadToken'),
    ]);

    expect(await leasesFileUploadUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await leasesFileUploadUpdatePage.getFileNameInput()).to.eq('fileName', 'Expected FileName value to be equals to fileName');
    expect(await leasesFileUploadUpdatePage.getPeriodFromInput()).to.eq(
      '2000-12-31',
      'Expected periodFrom value to be equals to 2000-12-31'
    );
    expect(await leasesFileUploadUpdatePage.getPeriodToInput()).to.eq('2000-12-31', 'Expected periodTo value to be equals to 2000-12-31');
    expect(await leasesFileUploadUpdatePage.getLeasesFileTypeIdInput()).to.eq('5', 'Expected leasesFileTypeId value to be equals to 5');
    expect(await leasesFileUploadUpdatePage.getDataFileInput()).to.endsWith(
      fileNameToUpload,
      'Expected DataFile value to be end with ' + fileNameToUpload
    );
    const selectedUploadSuccessful = leasesFileUploadUpdatePage.getUploadSuccessfulInput();
    if (await selectedUploadSuccessful.isSelected()) {
      await leasesFileUploadUpdatePage.getUploadSuccessfulInput().click();
      expect(await leasesFileUploadUpdatePage.getUploadSuccessfulInput().isSelected(), 'Expected uploadSuccessful not to be selected').to.be
        .false;
    } else {
      await leasesFileUploadUpdatePage.getUploadSuccessfulInput().click();
      expect(await leasesFileUploadUpdatePage.getUploadSuccessfulInput().isSelected(), 'Expected uploadSuccessful to be selected').to.be
        .true;
    }
    const selectedUploadProcessed = leasesFileUploadUpdatePage.getUploadProcessedInput();
    if (await selectedUploadProcessed.isSelected()) {
      await leasesFileUploadUpdatePage.getUploadProcessedInput().click();
      expect(await leasesFileUploadUpdatePage.getUploadProcessedInput().isSelected(), 'Expected uploadProcessed not to be selected').to.be
        .false;
    } else {
      await leasesFileUploadUpdatePage.getUploadProcessedInput().click();
      expect(await leasesFileUploadUpdatePage.getUploadProcessedInput().isSelected(), 'Expected uploadProcessed to be selected').to.be.true;
    }
    expect(await leasesFileUploadUpdatePage.getUploadTokenInput()).to.eq(
      'uploadToken',
      'Expected UploadToken value to be equals to uploadToken'
    );

    await leasesFileUploadUpdatePage.save();
    expect(await leasesFileUploadUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leasesFileUploadComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LeasesFileUpload', async () => {
    const nbButtonsBeforeDelete = await leasesFileUploadComponentsPage.countDeleteButtons();
    await leasesFileUploadComponentsPage.clickOnLastDeleteButton();

    leasesFileUploadDeleteDialog = new LeasesFileUploadDeleteDialog();
    expect(await leasesFileUploadDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Leases File Upload?');
    await leasesFileUploadDeleteDialog.clickOnConfirmButton();

    expect(await leasesFileUploadComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
