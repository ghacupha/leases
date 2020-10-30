import { Moment } from 'moment';

export interface ILeaseDetails {
  id?: number;
  leaseContractNumber?: string;
  incrementalBorrowingRate?: number;
  commencementDate?: Moment;
  leasePrepayments?: number;
  initialDirectCosts?: number;
  demolitionCosts?: number;
  assetAccountNumber?: string;
  liabilityAccountNumber?: string;
  depreciationAccountNumber?: string;
  interestAccountNumber?: string;
}

export class LeaseDetails implements ILeaseDetails {
  constructor(
    public id?: number,
    public leaseContractNumber?: string,
    public incrementalBorrowingRate?: number,
    public commencementDate?: Moment,
    public leasePrepayments?: number,
    public initialDirectCosts?: number,
    public demolitionCosts?: number,
    public assetAccountNumber?: string,
    public liabilityAccountNumber?: string,
    public depreciationAccountNumber?: string,
    public interestAccountNumber?: string
  ) {}
}
