import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Objet } from './objet.model';
import { ObjetPopupService } from './objet-popup.service';
import { ObjetService } from './objet.service';

@Component({
    selector: 'jhi-objet-delete-dialog',
    templateUrl: './objet-delete-dialog.component.html'
})
export class ObjetDeleteDialogComponent {

    objet: Objet;

    constructor(
        private objetService: ObjetService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.objetService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'objetListModification',
                content: 'Deleted an objet'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-objet-delete-popup',
    template: ''
})
export class ObjetDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private objetPopupService: ObjetPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.objetPopupService
                .open(ObjetDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
