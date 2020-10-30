import { Moment } from 'moment';

/**
 * This interface maintains the fields for navigating to the corresponding
 * reports
 */
export interface INavigationQuestionnaire {
  reportingPeriod?: Moment;
}

/**
 * This class maintains the fields for navigating to the corresponding
 * reports
 */
export class NavigationQuestionnaire implements INavigationQuestionnaire {
  constructor(public reportingPeriod?: Moment) {}
}
