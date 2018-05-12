import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pokedex } from './pokedex.model';
import { PokedexPopupService } from './pokedex-popup.service';
import { PokedexService } from './pokedex.service';

@Component({
    selector: 'jhi-pokedex-delete-dialog',
    templateUrl: './pokedex-delete-dialog.component.html'
})
export class PokedexDeleteDialogComponent {

    pokedex: Pokedex;

    constructor(
        private pokedexService: PokedexService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pokedexService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pokedexListModification',
                content: 'Deleted an pokedex'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pokedex-delete-popup',
    template: ''
})
export class PokedexDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pokedexPopupService: PokedexPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pokedexPopupService
                .open(PokedexDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
