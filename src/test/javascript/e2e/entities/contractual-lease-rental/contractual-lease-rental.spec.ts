import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ContractualLeaseRentalComponentsPage,
  ContractualLeaseRentalDeleteDialog,
  ContractualLeaseRentalUpdatePage,
} from './contractual-lease-rental.page-object';

const expect = chai.expect;

describe('ContractualLeaseRental e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contractualLeaseRentalComponentsPage: ContractualLeaseRentalComponentsPage;
  let contractualLeaseRentalUpdatePage: ContractualLeaseRentalUpdatePage;
  let contractualLeaseRentalDeleteDialog: ContractualLeaseRentalDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ContractualLeaseRentals', async () => {
    await navBarPage.goToEntity('contractual-lease-rental');
    contractualLeaseRentalComponentsPage = new ContractualLeaseRentalComponentsPage();
    await browser.wait(ec.visibilityOf(contractualLeaseRentalComponentsPage.title), 5000);
    expect(await contractualLeaseRentalComponentsPage.getTitle()).to.eq('Contractual Lease Rentals');
    await browser.wait(
      ec.or(ec.visibilityOf(contractualLeaseRentalComponentsPage.entities), ec.visibilityOf(contractualLeaseRentalComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ContractualLeaseRental page', async () => {
    await contractualLeaseRentalComponentsPage.clickOnCreateButton();
    contractualLeaseRentalUpdatePage = new ContractualLeaseRentalUpdatePage();
    expect(await contractualLeaseRentalUpdatePage.getPageTitle()).to.eq('Create or edit a Contractual Lease Rental');
    await contractualLeaseRentalUpdatePage.cancel();
  });

  it('should create and save ContractualLeaseRentals', async () => {
    const nbButtonsBeforeCreate = await contractualLeaseRentalComponentsPage.countDeleteButtons();

    await contractualLeaseRentalComponentsPage.clickOnCreateButton();

    await promise.all([
      contractualLeaseRentalUpdatePage.setLeaseContractNumberInput('leaseContractNumber'),
      contractualLeaseRentalUpdatePage.setRentalSequenceNumberInput('5'),
      contractualLeaseRentalUpdatePage.setLeaseRentalDateInput('2000-12-31'),
      contractualLeaseRentalUpdatePage.setLeaseRentalAmountInput('5'),
    ]);

    expect(await contractualLeaseRentalUpdatePage.getLeaseContractNumberInput()).to.eq(
      'leaseContractNumber',
      'Expected LeaseContractNumber value to be equals to leaseContractNumber'
    );
    expect(await contractualLeaseRentalUpdatePage.getRentalSequenceNumberInput()).to.eq(
      '5',
      'Expected rentalSequenceNumber value to be equals to 5'
    );
    expect(await contractualLeaseRentalUpdatePage.getLeaseRentalDateInput()).to.eq(
      '2000-12-31',
      'Expected leaseRentalDate value to be equals to 2000-12-31'
    );
    expect(await contractualLeaseRentalUpdatePage.getLeaseRentalAmountInput()).to.eq(
      '5',
      'Expected leaseRentalAmount value to be equals to 5'
    );

    await contractualLeaseRentalUpdatePage.save();
    expect(await contractualLeaseRentalUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contractualLeaseRentalComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ContractualLeaseRental', async () => {
    const nbButtonsBeforeDelete = await contractualLeaseRentalComponentsPage.countDeleteButtons();
    await contractualLeaseRentalComponentsPage.clickOnLastDeleteButton();

    contractualLeaseRentalDeleteDialog = new ContractualLeaseRentalDeleteDialog();
    expect(await contractualLeaseRentalDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Contractual Lease Rental?'
    );
    await contractualLeaseRentalDeleteDialog.clickOnConfirmButton();

    expect(await contractualLeaseRentalComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
