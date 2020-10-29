///
/// Leases - Leases management application
/// Copyright © 2020 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService, JhiAlert } from 'ng-jhipster';

@Component({
  selector: 'jhi-alert',
  template: ` <div class="alerts" role="alert">
    <div *ngFor="let alert of alerts" [ngClass]="setClasses(alert)">
      <ngb-alert *ngIf="alert && alert.type && alert.msg" [type]="alert.type" (close)="close(alert)">
        <pre [innerHTML]="alert.msg"></pre>
      </ngb-alert>
    </div>
  </div>`,
})
export class AlertComponent implements OnInit, OnDestroy {
  alerts: JhiAlert[] = [];

  constructor(private alertService: JhiAlertService) {}

  ngOnInit(): void {
    this.alerts = this.alertService.get();
  }

  setClasses(alert: JhiAlert): { [key: string]: boolean } {
    const classes = { 'jhi-toast': Boolean(alert.toast) };
    if (alert.position) {
      return { ...classes, [alert.position]: true };
    }
    return classes;
  }

  ngOnDestroy(): void {
    this.alertService.clear();
  }

  close(alert: JhiAlert): void {
    // NOSONAR can be removed after https://github.com/SonarSource/SonarJS/issues/1930 is resolved
    alert.close?.(this.alerts); // NOSONAR
  }
}
