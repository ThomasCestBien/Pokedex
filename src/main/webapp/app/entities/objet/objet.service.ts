import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Objet } from './objet.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Objet>;

@Injectable()
export class ObjetService {

    private resourceUrl =  SERVER_API_URL + 'api/objets';

    constructor(private http: HttpClient) { }

    create(objet: Objet): Observable<EntityResponseType> {
        const copy = this.convert(objet);
        return this.http.post<Objet>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(objet: Objet): Observable<EntityResponseType> {
        const copy = this.convert(objet);
        return this.http.put<Objet>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Objet>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Objet[]>> {
        const options = createRequestOption(req);
        return this.http.get<Objet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Objet[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Objet = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Objet[]>): HttpResponse<Objet[]> {
        const jsonResponse: Objet[] = res.body;
        const body: Objet[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Objet.
     */
    private convertItemFromServer(objet: Objet): Objet {
        const copy: Objet = Object.assign({}, objet);
        return copy;
    }

    /**
     * Convert a Objet to a JSON which can be sent to the server.
     */
    private convert(objet: Objet): Objet {
        const copy: Objet = Object.assign({}, objet);
        return copy;
    }
}
