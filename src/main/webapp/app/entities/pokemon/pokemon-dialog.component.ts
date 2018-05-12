import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Pokemon } from './pokemon.model';
import { PokemonPopupService } from './pokemon-popup.service';
import { PokemonService } from './pokemon.service';
import { Pokedex, PokedexService } from '../pokedex';

@Component({
    selector: 'jhi-pokemon-dialog',
    templateUrl: './pokemon-dialog.component.html'
})
export class PokemonDialogComponent implements OnInit {

    pokemon: Pokemon;
    isSaving: boolean;

    pokedexes: Pokedex[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private pokemonService: PokemonService,
        private pokedexService: PokedexService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pokedexService.query()
            .subscribe((res: HttpResponse<Pokedex[]>) => { this.pokedexes = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pokemon.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pokemonService.update(this.pokemon));
        } else {
            this.subscribeToSaveResponse(
                this.pokemonService.create(this.pokemon));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Pokemon>>) {
        result.subscribe((res: HttpResponse<Pokemon>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Pokemon) {
        this.eventManager.broadcast({ name: 'pokemonListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPokedexById(index: number, item: Pokedex) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-pokemon-popup',
    template: ''
})
export class PokemonPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pokemonPopupService: PokemonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pokemonPopupService
                    .open(PokemonDialogComponent as Component, params['id']);
            } else {
                this.pokemonPopupService
                    .open(PokemonDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
