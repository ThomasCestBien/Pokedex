import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pokedex } from './pokedex.model';
import { PokedexPopupService } from './pokedex-popup.service';
import { PokedexService } from './pokedex.service';

@Component({
    selector: 'jhi-pokedex-dialog',
    templateUrl: './pokedex-dialog.component.html'
})
export class PokedexDialogComponent implements OnInit {

    pokedex: Pokedex;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private pokedexService: PokedexService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.pokedex.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pokedexService.update(this.pokedex));
        } else {
            this.subscribeToSaveResponse(
                this.pokedexService.create(this.pokedex));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Pokedex>>) {
        result.subscribe((res: HttpResponse<Pokedex>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Pokedex) {
        this.eventManager.broadcast({ name: 'pokedexListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-pokedex-popup',
    template: ''
})
export class PokedexPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pokedexPopupService: PokedexPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pokedexPopupService
                    .open(PokedexDialogComponent as Component, params['id']);
            } else {
                this.pokedexPopupService
                    .open(PokedexDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
