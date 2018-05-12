import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokebaseSharedModule } from '../../shared';
import {
    ObjetService,
    ObjetPopupService,
    ObjetComponent,
    ObjetDetailComponent,
    ObjetDialogComponent,
    ObjetPopupComponent,
    ObjetDeletePopupComponent,
    ObjetDeleteDialogComponent,
    objetRoute,
    objetPopupRoute,
} from './';

const ENTITY_STATES = [
    ...objetRoute,
    ...objetPopupRoute,
];

@NgModule({
    imports: [
        PokebaseSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ObjetComponent,
        ObjetDetailComponent,
        ObjetDialogComponent,
        ObjetDeleteDialogComponent,
        ObjetPopupComponent,
        ObjetDeletePopupComponent,
    ],
    entryComponents: [
        ObjetComponent,
        ObjetDialogComponent,
        ObjetPopupComponent,
        ObjetDeleteDialogComponent,
        ObjetDeletePopupComponent,
    ],
    providers: [
        ObjetService,
        ObjetPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PokebaseObjetModule {}
