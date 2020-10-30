import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LeaseDetailsComponentsPage, LeaseDetailsDeleteDialog, LeaseDetailsUpdatePage } from './lease-details.page-object';

const expect = chai.expect;

describe('LeaseDetails e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseDetailsComponentsPage: LeaseDetailsComponentsPage;
  let leaseDetailsUpdatePage: LeaseDetailsUpdatePage;
  let leaseDetailsDeleteDialog: LeaseDetailsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseDetails', async () => {
    await navBarPage.goToEntity('lease-details');
    leaseDetailsComponentsPage = new LeaseDetailsComponentsPage();
    await browser.wait(ec.visibilityOf(leaseDetailsComponentsPage.title), 5000);
    expect(await leaseDetailsComponentsPage.getTitle()).to.eq('Lease Details');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseDetailsComponentsPage.entities), ec.visibilityOf(leaseDetailsComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseDetails page', async () => {
    await leaseDetailsComponentsPage.clickOnCreateButton();
    leaseDetailsUpdatePage = new LeaseDetailsUpdatePage();
    expect(await leaseDetailsUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Details');
    await leaseDetailsUpdatePage.cancel();
  });

  it('should create and save LeaseDetails', async () => {
    const nbButtonsBeforeCreate = await leaseDetailsComponentsPage.countDeleteButtons();

    await leaseDetailsComponentsPage.clickOnCreateButton();

    await promise.all([
      leaseDetailsUpdatePage.setLeaseContractNumberInput('leaseContractNumber'),
      leaseDetailsUpdatePage.setIncrementalBorrowingRateInput('5'),
      leaseDetailsUpdatePage.setCommencementDateInput('2000-12-31'),
      leaseDetailsUpdatePage.setLeasePrepaymentsInput('5'),
      leaseDetailsUpdatePage.setInitialDirectCostsInput('5'),
      leaseDetailsUpdatePage.setDemolitionCostsInput('5'),
      leaseDetailsUpdatePage.setAssetAccountNumberInput('assetAccountNumber'),
      leaseDetailsUpdatePage.setLiabilityAccountNumberInput('liabilityAccountNumber'),
      leaseDetailsUpdatePage.setDepreciationAccountNumberInput('depreciationAccountNumber'),
      leaseDetailsUpdatePage.setInterestAccountNumberInput('interestAccountNumber'),
    ]);

    expect(await leaseDetailsUpdatePage.getLeaseContractNumberInput()).to.eq(
      'leaseContractNumber',
      'Expected LeaseContractNumber value to be equals to leaseContractNumber'
    );
    expect(await leaseDetailsUpdatePage.getIncrementalBorrowingRateInput()).to.eq(
      '5',
      'Expected incrementalBorrowingRate value to be equals to 5'
    );
    expect(await leaseDetailsUpdatePage.getCommencementDateInput()).to.eq(
      '2000-12-31',
      'Expected commencementDate value to be equals to 2000-12-31'
    );
    expect(await leaseDetailsUpdatePage.getLeasePrepaymentsInput()).to.eq('5', 'Expected leasePrepayments value to be equals to 5');
    expect(await leaseDetailsUpdatePage.getInitialDirectCostsInput()).to.eq('5', 'Expected initialDirectCosts value to be equals to 5');
    expect(await leaseDetailsUpdatePage.getDemolitionCostsInput()).to.eq('5', 'Expected demolitionCosts value to be equals to 5');
    expect(await leaseDetailsUpdatePage.getAssetAccountNumberInput()).to.eq(
      'assetAccountNumber',
      'Expected AssetAccountNumber value to be equals to assetAccountNumber'
    );
    expect(await leaseDetailsUpdatePage.getLiabilityAccountNumberInput()).to.eq(
      'liabilityAccountNumber',
      'Expected LiabilityAccountNumber value to be equals to liabilityAccountNumber'
    );
    expect(await leaseDetailsUpdatePage.getDepreciationAccountNumberInput()).to.eq(
      'depreciationAccountNumber',
      'Expected DepreciationAccountNumber value to be equals to depreciationAccountNumber'
    );
    expect(await leaseDetailsUpdatePage.getInterestAccountNumberInput()).to.eq(
      'interestAccountNumber',
      'Expected InterestAccountNumber value to be equals to interestAccountNumber'
    );

    await leaseDetailsUpdatePage.save();
    expect(await leaseDetailsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leaseDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last LeaseDetails', async () => {
    const nbButtonsBeforeDelete = await leaseDetailsComponentsPage.countDeleteButtons();
    await leaseDetailsComponentsPage.clickOnLastDeleteButton();

    leaseDetailsDeleteDialog = new LeaseDetailsDeleteDialog();
    expect(await leaseDetailsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Lease Details?');
    await leaseDetailsDeleteDialog.clickOnConfirmButton();

    expect(await leaseDetailsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
