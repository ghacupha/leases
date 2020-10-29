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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { LoginModalService } from 'app/core/login/login-modal.service';

// Mock class for NgbModalRef
export class MockNgbModalRef {
  result: Promise<any> = new Promise(resolve => resolve('x'));
}

describe('Service Tests', () => {
  describe('Login Modal Service', () => {
    let service: LoginModalService;
    let modalService: NgbModal;

    beforeEach(() => {
      service = TestBed.get(LoginModalService);
      modalService = TestBed.get(NgbModal);
    });

    describe('Service methods', () => {
      it('Should call open method for NgbModal when open method is called', () => {
        // GIVEN
        const mockModalRef: MockNgbModalRef = new MockNgbModalRef();
        spyOn(modalService, 'open').and.returnValue(mockModalRef);

        // WHEN
        service.open();

        // THEN
        expect(modalService.open).toHaveBeenCalled();
      });

      it('Should call open method for NgbModal one time when open method is called twice', () => {
        // GIVEN
        const mockModalRef: MockNgbModalRef = new MockNgbModalRef();
        spyOn(modalService, 'open').and.returnValue(mockModalRef);

        // WHEN
        service.open();
        service.open();

        // THEN
        expect(modalService.open).toHaveBeenCalledTimes(1);
      });
    });
  });
});
