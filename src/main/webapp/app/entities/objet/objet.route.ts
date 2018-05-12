import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ObjetComponent } from './objet.component';
import { ObjetDetailComponent } from './objet-detail.component';
import { ObjetPopupComponent } from './objet-dialog.component';
import { ObjetDeletePopupComponent } from './objet-delete-dialog.component';

export const objetRoute: Routes = [
    {
        path: 'objet',
        component: ObjetComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Objets'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'objet/:id',
        component: ObjetDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Objets'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const objetPopupRoute: Routes = [
    {
        path: 'objet-new',
        component: ObjetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Objets'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'objet/:id/edit',
        component: ObjetPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Objets'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'objet/:id/delete',
        component: ObjetDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Objets'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
