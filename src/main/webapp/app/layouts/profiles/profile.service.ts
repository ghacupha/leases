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

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, shareReplay } from 'rxjs/operators';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ProfileInfo, InfoResponse } from './profile-info.model';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private infoUrl = SERVER_API_URL + 'management/info';
  private profileInfo$!: Observable<ProfileInfo>;

  constructor(private http: HttpClient) {}

  getProfileInfo(): Observable<ProfileInfo> {
    if (this.profileInfo$) {
      return this.profileInfo$;
    }

    this.profileInfo$ = this.http.get<InfoResponse>(this.infoUrl).pipe(
      map((response: InfoResponse) => {
        const profileInfo: ProfileInfo = {
          activeProfiles: response.activeProfiles,
          inProduction: response.activeProfiles && response.activeProfiles.includes('prod'),
          swaggerEnabled: response.activeProfiles && response.activeProfiles.includes('swagger'),
        };
        if (response.activeProfiles && response['display-ribbon-on-profiles']) {
          const displayRibbonOnProfiles = response['display-ribbon-on-profiles'].split(',');
          const ribbonProfiles = displayRibbonOnProfiles.filter(
            profile => response.activeProfiles && response.activeProfiles.includes(profile)
          );
          if (ribbonProfiles.length > 0) {
            profileInfo.ribbonEnv = ribbonProfiles[0];
          }
        }
        return profileInfo;
      }),
      shareReplay()
    );
    return this.profileInfo$;
  }
}
