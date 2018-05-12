import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Pokedex } from './pokedex.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Pokedex>;

@Injectable()
export class PokedexService {

    private resourceUrl =  SERVER_API_URL + 'api/pokedexes';

    constructor(private http: HttpClient) { }

    create(pokedex: Pokedex): Observable<EntityResponseType> {
        const copy = this.convert(pokedex);
        return this.http.post<Pokedex>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(pokedex: Pokedex): Observable<EntityResponseType> {
        const copy = this.convert(pokedex);
        return this.http.put<Pokedex>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Pokedex>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Pokedex[]>> {
        const options = createRequestOption(req);
        return this.http.get<Pokedex[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Pokedex[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Pokedex = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Pokedex[]>): HttpResponse<Pokedex[]> {
        const jsonResponse: Pokedex[] = res.body;
        const body: Pokedex[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Pokedex.
     */
    private convertItemFromServer(pokedex: Pokedex): Pokedex {
        const copy: Pokedex = Object.assign({}, pokedex);
        return copy;
    }

    /**
     * Convert a Pokedex to a JSON which can be sent to the server.
     */
    private convert(pokedex: Pokedex): Pokedex {
        const copy: Pokedex = Object.assign({}, pokedex);
        return copy;
    }
}
