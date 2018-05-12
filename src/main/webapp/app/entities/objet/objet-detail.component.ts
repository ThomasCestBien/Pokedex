import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Objet } from './objet.model';
import { ObjetService } from './objet.service';

@Component({
    selector: 'jhi-objet-detail',
    templateUrl: './objet-detail.component.html'
})
export class ObjetDetailComponent implements OnInit, OnDestroy {

    objet: Objet;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private objetService: ObjetService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInObjets();
    }

    load(id) {
        this.objetService.find(id)
            .subscribe((objetResponse: HttpResponse<Objet>) => {
                this.objet = objetResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInObjets() {
        this.eventSubscriber = this.eventManager.subscribe(
            'objetListModification',
            (response) => this.load(this.objet.id)
        );
    }
}
