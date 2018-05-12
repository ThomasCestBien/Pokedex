import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Objet } from './objet.model';
import { ObjetPopupService } from './objet-popup.service';
import { ObjetService } from './objet.service';
import { Pokedex, PokedexService } from '../pokedex';

@Component({
    selector: 'jhi-objet-dialog',
    templateUrl: './objet-dialog.component.html'
})
export class ObjetDialogComponent implements OnInit {

    objet: Objet;
    isSaving: boolean;

    pokedexes: Pokedex[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private objetService: ObjetService,
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
        if (this.objet.id !== undefined) {
            this.subscribeToSaveResponse(
                this.objetService.update(this.objet));
        } else {
            this.subscribeToSaveResponse(
                this.objetService.create(this.objet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Objet>>) {
        result.subscribe((res: HttpResponse<Objet>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Objet) {
        this.eventManager.broadcast({ name: 'objetListModification', content: 'OK'});
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
    selector: 'jhi-objet-popup',
    template: ''
})
export class ObjetPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private objetPopupService: ObjetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.objetPopupService
                    .open(ObjetDialogComponent as Component, params['id']);
            } else {
                this.objetPopupService
                    .open(ObjetDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
