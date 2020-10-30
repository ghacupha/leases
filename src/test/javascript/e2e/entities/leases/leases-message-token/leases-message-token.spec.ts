import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  LeasesMessageTokenComponentsPage,
  LeasesMessageTokenDeleteDialog,
  LeasesMessageTokenUpdatePage,
} from './leases-message-token.page-object';

const expect = chai.expect;

describe('LeasesMessageToken e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leasesMessageTokenComponentsPage: LeasesMessageTokenComponentsPage;
  let leasesMessageTokenUpdatePage: LeasesMessageTokenUpdatePage;
  let leasesMessageTokenDeleteDialog: LeasesMessageTokenDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeasesMessageTokens', async () => {
    await navBarPage.goToEntity('leases-message-token');
    leasesMessageTokenComponentsPage = new LeasesMessageTokenComponentsPage();
    await browser.wait(ec.visibilityOf(leasesMessageTokenComponentsPage.title), 5000);
    expect(await leasesMessageTokenComponentsPage.getTitle()).to.eq('Leases Message Tokens');
    await browser.wait(
      ec.or(ec.visibilityOf(leasesMessageTokenComponentsPage.entities), ec.visibilityOf(leasesMessageTokenComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeasesMessageToken page', async () => {
    await leasesMessageTokenComponentsPage.clickOnCreateButton();
    leasesMessageTokenUpdatePage = new LeasesMessageTokenUpdatePage();
    expect(await leasesMessageTokenUpdatePage.getPageTitle()).to.eq('Create or edit a Leases Message Token');
    await leasesMessageTokenUpdatePage.cancel();
  });

  it('should create and save LeasesMessageTokens', async () => {
    const nbButtonsBeforeCreate = await leasesMessageTokenComponentsPage.countDeleteButtons();

    await leasesMessageTokenComponentsPage.clickOnCreateButton();

    await promise.all([
      leasesMessageTokenUpdatePage.setDescriptionInput('description'),
      leasesMessageTokenUpdatePage.setTimeSentInput('5'),
      leasesMessageTokenUpdatePage.setTokenValueInput('tokenValue'),
    ]);

    expect(await leasesMessageTokenUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await leasesMessageTokenUpdatePage.getTimeSentInput()).to.eq('5', 'Expected timeSent value to be equals to 5');
    expect(await leasesMessageTokenUpdatePage.getTokenValueInput()).to.eq(
      'tokenValue',
      'Expected TokenValue value to be equals to tokenValue'
    );
    const selectedReceived = leasesMessageTokenUpdatePage.getReceivedInput();
    if (await selectedReceived.isSelected()) {
      await leasesMessageTokenUpdatePage.getReceivedInput().click();
      expect(await leasesMessageTokenUpdatePage.getReceivedInput().isSelected(), 'Expected received not to be selected').to.be.false;
    } else {
      await leasesMessageTokenUpdatePage.getReceivedInput().click();
      expect(await leasesMessageTokenUpdatePage.getReceivedInput().isSelected(), 'Expected received to be selected').to.be.true;
    }
    const selectedActioned = leasesMessageTokenUpdatePage.getActionedInput();
    if (await selectedActioned.isSelected()) {
      await leasesMessageTokenUpdatePage.getActionedInput().click();
      expect(await leasesMessageTokenUpdatePage.getActionedInput().isSelected(), 'Expected actioned not to be selected').to.be.false;
    } else {
      await leasesMessageTokenUpdatePage.getActionedInput().click();
      expect(await leasesMessageTokenUpdatePage.getActionedInput().isSelected(), 'Expected actioned to be selected').to.be.true;
    }
    const selectedContentFullyEnqueued = leasesMessageTokenUpdatePage.getContentFullyEnqueuedInput();
    if (await selectedContentFullyEnqueued.isSelected()) {
      await leasesMessageTokenUpdatePage.getContentFullyEnqueuedInput().click();
      expect(
        await leasesMessageTokenUpdatePage.getContentFullyEnqueuedInput().isSelected(),
        'Expected contentFullyEnqueued not to be selected'
      ).to.be.false;
    } else {
      await leasesMessageTokenUpdatePage.getContentFullyEnqueuedInput().click();
      expect(await leasesMessageTokenUpdatePage.getContentFullyEnqueuedInput().isSelected(), 'Expected contentFullyEnqueued to be selected')
        .to.be.true;
    }

    await leasesMessageTokenUpdatePage.save();
    expect(await leasesMessageTokenUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leasesMessageTokenComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LeasesMessageToken', async () => {
    const nbButtonsBeforeDelete = await leasesMessageTokenComponentsPage.countDeleteButtons();
    await leasesMessageTokenComponentsPage.clickOnLastDeleteButton();

    leasesMessageTokenDeleteDialog = new LeasesMessageTokenDeleteDialog();
    expect(await leasesMessageTokenDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Leases Message Token?');
    await leasesMessageTokenDeleteDialog.clickOnConfirmButton();

    expect(await leasesMessageTokenComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
