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

import { Component, OnInit } from '@angular/core';

import { Log, LoggersResponse, Logger, Level } from './log.model';
import { LogsService } from './logs.service';

@Component({
  selector: 'jhi-logs',
  templateUrl: './logs.component.html',
})
export class LogsComponent implements OnInit {
  loggers?: Log[];
  filter = '';
  orderProp = 'name';
  reverse = false;

  constructor(private logsService: LogsService) {}

  ngOnInit(): void {
    this.findAndExtractLoggers();
  }

  changeLevel(name: string, level: Level): void {
    this.logsService.changeLevel(name, level).subscribe(() => this.findAndExtractLoggers());
  }

  private findAndExtractLoggers(): void {
    this.logsService
      .findAll()
      .subscribe(
        (response: LoggersResponse) =>
          (this.loggers = Object.entries(response.loggers).map((logger: [string, Logger]) => new Log(logger[0], logger[1].effectiveLevel)))
      );
  }
}
