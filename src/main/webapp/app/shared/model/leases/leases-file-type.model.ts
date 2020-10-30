import { LeasesFileMediumTypes } from 'app/shared/model/enumerations/leases-file-medium-types.model';
import { LeasesFileModelType } from 'app/shared/model/enumerations/leases-file-model-type.model';

export interface ILeasesFileType {
  id?: number;
  leasesFileTypeName?: string;
  leasesFileMediumType?: LeasesFileMediumTypes;
  description?: string;
  fileTemplateContentType?: string;
  fileTemplate?: any;
  leasesfileType?: LeasesFileModelType;
}

export class LeasesFileType implements ILeasesFileType {
  constructor(
    public id?: number,
    public leasesFileTypeName?: string,
    public leasesFileMediumType?: LeasesFileMediumTypes,
    public description?: string,
    public fileTemplateContentType?: string,
    public fileTemplate?: any,
    public leasesfileType?: LeasesFileModelType
  ) {}
}
