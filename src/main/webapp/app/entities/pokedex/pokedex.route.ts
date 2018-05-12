import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PokedexComponent } from './pokedex.component';
import { PokedexDetailComponent } from './pokedex-detail.component';
import { PokedexPopupComponent } from './pokedex-dialog.component';
import { PokedexDeletePopupComponent } from './pokedex-delete-dialog.component';

export const pokedexRoute: Routes = [
    {
        path: 'pokedex',
        component: PokedexComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokedexes'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pokedex/:id',
        component: PokedexDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokedexes'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pokedexPopupRoute: Routes = [
    {
        path: 'pokedex-new',
        component: PokedexPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokedexes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pokedex/:id/edit',
        component: PokedexPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokedexes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pokedex/:id/delete',
        component: PokedexDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Pokedexes'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
