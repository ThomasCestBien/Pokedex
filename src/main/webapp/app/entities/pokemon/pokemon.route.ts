import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PokemonComponent } from './pokemon.component';
import { PokemonDetailComponent } from './pokemon-detail.component';
import { PokemonPopupComponent } from './pokemon-dialog.component';
import { PokemonDeletePopupComponent } from './pokemon-delete-dialog.component';

export const pokemonRoute: Routes = [
    {
        path: 'pokemon',
        component: PokemonComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokemons'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pokemon/:id',
        component: PokemonDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokemons'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pokemonPopupRoute: Routes = [
    {
        path: 'pokemon-new',
        component: PokemonPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokemons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pokemon/:id/edit',
        component: PokemonPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokemons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pokemon/:id/delete',
        component: PokemonDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokemons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
