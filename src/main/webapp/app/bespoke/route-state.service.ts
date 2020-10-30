import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';

/**
 * Very mundane yet useful for sending route state data to the component
 * we are navigating to even though the component does not have a parent
 * child relationship with the originator of data
 * The service is generic and transmits data of type T
 */
@Injectable({
  providedIn: 'root',
})
export class RouteStateService<T> {
  private _data!: T;

  constructor(private log: NGXLogger) {}

  reset(): void {
    this.log.debug('Resetting route-state...standby for cleanup');
    // this._data = undefined;
  }

  public get data(): T {
    this.log.debug(`Data requested... returning ${this._data}`);
    return this._data;
  }

  public set data(value: T) {
    this._data = value;
    this.log.debug(`Setting data for route states ${this._data}`);
  }
}
