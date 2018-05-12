import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Pokemon } from './pokemon.model';
import { PokemonService } from './pokemon.service';

@Component({
    selector: 'jhi-pokemon-detail',
    templateUrl: './pokemon-detail.component.html'
})
export class PokemonDetailComponent implements OnInit, OnDestroy {

    pokemon: Pokemon;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pokemonService: PokemonService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPokemons();
    }

    load(id) {
        this.pokemonService.find(id)
            .subscribe((pokemonResponse: HttpResponse<Pokemon>) => {
                this.pokemon = pokemonResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPokemons() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pokemonListModification',
            (response) => this.load(this.pokemon.id)
        );
    }
}
