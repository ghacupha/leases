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

import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { flatMap } from 'rxjs/operators';

import { MetricsService, Metrics, MetricsKey, ThreadDump, Thread } from './metrics.service';

@Component({
  selector: 'jhi-metrics',
  templateUrl: './metrics.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MetricsComponent implements OnInit {
  metrics?: Metrics;
  threads?: Thread[];
  updatingMetrics = true;

  constructor(private metricsService: MetricsService, private changeDetector: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.updatingMetrics = true;
    this.metricsService
      .getMetrics()
      .pipe(
        flatMap(
          () => this.metricsService.threadDump(),
          (metrics: Metrics, threadDump: ThreadDump) => {
            this.metrics = metrics;
            this.threads = threadDump.threads;
            this.updatingMetrics = false;
            this.changeDetector.detectChanges();
          }
        )
      )
      .subscribe();
  }

  metricsKeyExists(key: MetricsKey): boolean {
    return this.metrics && this.metrics[key];
  }

  metricsKeyExistsAndObjectNotEmpty(key: MetricsKey): boolean {
    return this.metrics && this.metrics[key] && JSON.stringify(this.metrics[key]) !== '{}';
  }
}
