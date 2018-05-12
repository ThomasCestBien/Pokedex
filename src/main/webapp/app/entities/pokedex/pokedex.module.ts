import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokebaseSharedModule } from '../../shared';
import {
    PokedexService,
    PokedexPopupService,
    PokedexComponent,
    PokedexDetailComponent,
    PokedexDialogComponent,
    PokedexPopupComponent,
    PokedexDeletePopupComponent,
    PokedexDeleteDialogComponent,
    pokedexRoute,
    pokedexPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pokedexRoute,
    ...pokedexPopupRoute,
];

@NgModule({
    imports: [
        PokebaseSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PokedexComponent,
        PokedexDetailComponent,
        PokedexDialogComponent,
        PokedexDeleteDialogComponent,
        PokedexPopupComponent,
        PokedexDeletePopupComponent,
    ],
    entryComponents: [
        PokedexComponent,
        PokedexDialogComponent,
        PokedexPopupComponent,
        PokedexDeleteDialogComponent,
        PokedexDeletePopupComponent,
    ],
    providers: [
        PokedexService,
        PokedexPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PokebasePokedexModule {}
