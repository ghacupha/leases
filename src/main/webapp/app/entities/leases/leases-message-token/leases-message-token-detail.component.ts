import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';

@Component({
  selector: 'jhi-leases-message-token-detail',
  templateUrl: './leases-message-token-detail.component.html',
})
export class LeasesMessageTokenDetailComponent implements OnInit {
  leasesMessageToken: ILeasesMessageToken | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasesMessageToken }) => (this.leasesMessageToken = leasesMessageToken));
  }

  previousState(): void {
    window.history.back();
  }
}
