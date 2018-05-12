import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Pokedex } from './pokedex.model';
import { PokedexService } from './pokedex.service';

@Component({
    selector: 'jhi-pokedex-detail',
    templateUrl: './pokedex-detail.component.html'
})
export class PokedexDetailComponent implements OnInit, OnDestroy {

    pokedex: Pokedex;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pokedexService: PokedexService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPokedexes();
    }

    load(id) {
        this.pokedexService.find(id)
            .subscribe((pokedexResponse: HttpResponse<Pokedex>) => {
                this.pokedex = pokedexResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPokedexes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pokedexListModification',
            (response) => this.load(this.pokedex.id)
        );
    }
}
