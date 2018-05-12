import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PokebaseSharedModule } from '../../shared';
import {
    PokemonService,
    PokemonPopupService,
    PokemonComponent,
    PokemonDetailComponent,
    PokemonDialogComponent,
    PokemonPopupComponent,
    PokemonDeletePopupComponent,
    PokemonDeleteDialogComponent,
    pokemonRoute,
    pokemonPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pokemonRoute,
    ...pokemonPopupRoute,
];

@NgModule({
    imports: [
        PokebaseSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PokemonComponent,
        PokemonDetailComponent,
        PokemonDialogComponent,
        PokemonDeleteDialogComponent,
        PokemonPopupComponent,
        PokemonDeletePopupComponent,
    ],
    entryComponents: [
        PokemonComponent,
        PokemonDialogComponent,
        PokemonPopupComponent,
        PokemonDeleteDialogComponent,
        PokemonDeletePopupComponent,
    ],
    providers: [
        PokemonService,
        PokemonPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PokebasePokemonModule {}
