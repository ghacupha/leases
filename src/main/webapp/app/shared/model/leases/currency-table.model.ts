import { CurrencyLocality } from 'app/shared/model/enumerations/currency-locality.model';

export interface ICurrencyTable {
  id?: number;
  currencyCode?: string;
  locality?: CurrencyLocality;
  currencyName?: string;
  country?: string;
}

export class CurrencyTable implements ICurrencyTable {
  constructor(
    public id?: number,
    public currencyCode?: string,
    public locality?: CurrencyLocality,
    public currencyName?: string,
    public country?: string
  ) {}
}
