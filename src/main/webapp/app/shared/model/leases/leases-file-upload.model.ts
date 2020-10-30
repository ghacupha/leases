import { Moment } from 'moment';

export interface ILeasesFileUpload {
  id?: number;
  description?: string;
  fileName?: string;
  periodFrom?: Moment;
  periodTo?: Moment;
  leasesFileTypeId?: number;
  dataFileContentType?: string;
  dataFile?: any;
  uploadSuccessful?: boolean;
  uploadProcessed?: boolean;
  uploadToken?: string;
}

export class LeasesFileUpload implements ILeasesFileUpload {
  constructor(
    public id?: number,
    public description?: string,
    public fileName?: string,
    public periodFrom?: Moment,
    public periodTo?: Moment,
    public leasesFileTypeId?: number,
    public dataFileContentType?: string,
    public dataFile?: any,
    public uploadSuccessful?: boolean,
    public uploadProcessed?: boolean,
    public uploadToken?: string
  ) {
    this.uploadSuccessful = this.uploadSuccessful || false;
    this.uploadProcessed = this.uploadProcessed || false;
  }
}
