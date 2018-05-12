import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pokedex } from './pokedex.model';
import { PokedexService } from './pokedex.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-pokedex',
    templateUrl: './pokedex.component.html'
})
export class PokedexComponent implements OnInit, OnDestroy {
pokedexes: Pokedex[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private pokedexService: PokedexService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.pokedexService.query().subscribe(
            (res: HttpResponse<Pokedex[]>) => {
                this.pokedexes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPokedexes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Pokedex) {
        return item.id;
    }
    registerChangeInPokedexes() {
        this.eventSubscriber = this.eventManager.subscribe('pokedexListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
