import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LeasesFileTypeComponentsPage, LeasesFileTypeDeleteDialog, LeasesFileTypeUpdatePage } from './leases-file-type.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('LeasesFileType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leasesFileTypeComponentsPage: LeasesFileTypeComponentsPage;
  let leasesFileTypeUpdatePage: LeasesFileTypeUpdatePage;
  let leasesFileTypeDeleteDialog: LeasesFileTypeDeleteDialog;
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

  it('should load LeasesFileTypes', async () => {
    await navBarPage.goToEntity('leases-file-type');
    leasesFileTypeComponentsPage = new LeasesFileTypeComponentsPage();
    await browser.wait(ec.visibilityOf(leasesFileTypeComponentsPage.title), 5000);
    expect(await leasesFileTypeComponentsPage.getTitle()).to.eq('Leases File Types');
    await browser.wait(
      ec.or(ec.visibilityOf(leasesFileTypeComponentsPage.entities), ec.visibilityOf(leasesFileTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeasesFileType page', async () => {
    await leasesFileTypeComponentsPage.clickOnCreateButton();
    leasesFileTypeUpdatePage = new LeasesFileTypeUpdatePage();
    expect(await leasesFileTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Leases File Type');
    await leasesFileTypeUpdatePage.cancel();
  });

  it('should create and save LeasesFileTypes', async () => {
    const nbButtonsBeforeCreate = await leasesFileTypeComponentsPage.countDeleteButtons();

    await leasesFileTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      leasesFileTypeUpdatePage.setLeasesFileTypeNameInput('leasesFileTypeName'),
      leasesFileTypeUpdatePage.leasesFileMediumTypeSelectLastOption(),
      leasesFileTypeUpdatePage.setDescriptionInput('description'),
      leasesFileTypeUpdatePage.setFileTemplateInput(absolutePath),
      leasesFileTypeUpdatePage.leasesfileTypeSelectLastOption(),
    ]);

    expect(await leasesFileTypeUpdatePage.getLeasesFileTypeNameInput()).to.eq(
      'leasesFileTypeName',
      'Expected LeasesFileTypeName value to be equals to leasesFileTypeName'
    );
    expect(await leasesFileTypeUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await leasesFileTypeUpdatePage.getFileTemplateInput()).to.endsWith(
      fileNameToUpload,
      'Expected FileTemplate value to be end with ' + fileNameToUpload
    );

    await leasesFileTypeUpdatePage.save();
    expect(await leasesFileTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leasesFileTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LeasesFileType', async () => {
    const nbButtonsBeforeDelete = await leasesFileTypeComponentsPage.countDeleteButtons();
    await leasesFileTypeComponentsPage.clickOnLastDeleteButton();

    leasesFileTypeDeleteDialog = new LeasesFileTypeDeleteDialog();
    expect(await leasesFileTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Leases File Type?');
    await leasesFileTypeDeleteDialog.clickOnConfirmButton();

    expect(await leasesFileTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
