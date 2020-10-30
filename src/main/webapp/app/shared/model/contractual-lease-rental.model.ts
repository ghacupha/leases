import { Moment } from 'moment';

export interface IContractualLeaseRental {
  id?: number;
  leaseContractNumber?: string;
  rentalSequenceNumber?: number;
  leaseRentalDate?: Moment;
  leaseRentalAmount?: number;
}

export class ContractualLeaseRental implements IContractualLeaseRental {
  constructor(
    public id?: number,
    public leaseContractNumber?: string,
    public rentalSequenceNumber?: number,
    public leaseRentalDate?: Moment,
    public leaseRentalAmount?: number
  ) {}
}
