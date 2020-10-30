import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CurrencyTableComponentsPage, CurrencyTableDeleteDialog, CurrencyTableUpdatePage } from './currency-table.page-object';

const expect = chai.expect;

describe('CurrencyTable e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let currencyTableComponentsPage: CurrencyTableComponentsPage;
  let currencyTableUpdatePage: CurrencyTableUpdatePage;
  let currencyTableDeleteDialog: CurrencyTableDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CurrencyTables', async () => {
    await navBarPage.goToEntity('currency-table');
    currencyTableComponentsPage = new CurrencyTableComponentsPage();
    await browser.wait(ec.visibilityOf(currencyTableComponentsPage.title), 5000);
    expect(await currencyTableComponentsPage.getTitle()).to.eq('Currency Tables');
    await browser.wait(
      ec.or(ec.visibilityOf(currencyTableComponentsPage.entities), ec.visibilityOf(currencyTableComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CurrencyTable page', async () => {
    await currencyTableComponentsPage.clickOnCreateButton();
    currencyTableUpdatePage = new CurrencyTableUpdatePage();
    expect(await currencyTableUpdatePage.getPageTitle()).to.eq('Create or edit a Currency Table');
    await currencyTableUpdatePage.cancel();
  });

  it('should create and save CurrencyTables', async () => {
    const nbButtonsBeforeCreate = await currencyTableComponentsPage.countDeleteButtons();

    await currencyTableComponentsPage.clickOnCreateButton();

    await promise.all([
      currencyTableUpdatePage.setCurrencyCodeInput('currencyCode'),
      currencyTableUpdatePage.localitySelectLastOption(),
      currencyTableUpdatePage.setCurrencyNameInput('currencyName'),
      currencyTableUpdatePage.setCountryInput('country'),
    ]);

    expect(await currencyTableUpdatePage.getCurrencyCodeInput()).to.eq(
      'currencyCode',
      'Expected CurrencyCode value to be equals to currencyCode'
    );
    expect(await currencyTableUpdatePage.getCurrencyNameInput()).to.eq(
      'currencyName',
      'Expected CurrencyName value to be equals to currencyName'
    );
    expect(await currencyTableUpdatePage.getCountryInput()).to.eq('country', 'Expected Country value to be equals to country');

    await currencyTableUpdatePage.save();
    expect(await currencyTableUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await currencyTableComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CurrencyTable', async () => {
    const nbButtonsBeforeDelete = await currencyTableComponentsPage.countDeleteButtons();
    await currencyTableComponentsPage.clickOnLastDeleteButton();

    currencyTableDeleteDialog = new CurrencyTableDeleteDialog();
    expect(await currencyTableDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Currency Table?');
    await currencyTableDeleteDialog.clickOnConfirmButton();

    expect(await currencyTableComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
