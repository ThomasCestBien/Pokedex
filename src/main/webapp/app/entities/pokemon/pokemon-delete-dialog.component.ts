import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pokemon } from './pokemon.model';
import { PokemonPopupService } from './pokemon-popup.service';
import { PokemonService } from './pokemon.service';

@Component({
    selector: 'jhi-pokemon-delete-dialog',
    templateUrl: './pokemon-delete-dialog.component.html'
})
export class PokemonDeleteDialogComponent {

    pokemon: Pokemon;

    constructor(
        private pokemonService: PokemonService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pokemonService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pokemonListModification',
                content: 'Deleted an pokemon'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pokemon-delete-popup',
    template: ''
})
export class PokemonDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pokemonPopupService: PokemonPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pokemonPopupService
                .open(PokemonDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
