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

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MetricsService, Metrics, ThreadDump } from 'app/admin/metrics/metrics.service';
import { SERVER_API_URL } from 'app/app.constants';

describe('Service Tests', () => {
  describe('Logs Service', () => {
    let service: MetricsService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      service = TestBed.get(MetricsService);
      httpMock = TestBed.get(HttpTestingController);
    });

    afterEach(() => {
      httpMock.verify();
    });

    describe('Service methods', () => {
      it('should call correct URL', () => {
        service.getMetrics().subscribe();

        const req = httpMock.expectOne({ method: 'GET' });
        const resourceUrl = SERVER_API_URL + 'management/jhimetrics';
        expect(req.request.url).toEqual(resourceUrl);
      });

      it('should return Metrics', () => {
        let expectedResult: Metrics | null = null;
        const metrics: Metrics = {
          jvm: {},
          'http.server.requests': {},
          cache: {},
          services: {},
          databases: {},
          garbageCollector: {},
          processMetrics: {},
        };

        service.getMetrics().subscribe(received => {
          expectedResult = received;
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(metrics);
        expect(expectedResult).toEqual(metrics);
      });

      it('should return Thread Dump', () => {
        let expectedResult: ThreadDump | null = null;
        const dump: ThreadDump = { threads: [{ name: 'test1', threadState: 'RUNNABLE' }] };

        service.threadDump().subscribe(received => {
          expectedResult = received;
        });

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(dump);
        expect(expectedResult).toEqual(dump);
      });
    });
  });
});
