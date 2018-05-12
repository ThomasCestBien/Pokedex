import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Pokemon } from './pokemon.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Pokemon>;

@Injectable()
export class PokemonService {

    private resourceUrl =  SERVER_API_URL + 'api/pokemons';

    constructor(private http: HttpClient) { }

    create(pokemon: Pokemon): Observable<EntityResponseType> {
        const copy = this.convert(pokemon);
        return this.http.post<Pokemon>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(pokemon: Pokemon): Observable<EntityResponseType> {
        const copy = this.convert(pokemon);
        return this.http.put<Pokemon>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Pokemon>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Pokemon[]>> {
        const options = createRequestOption(req);
        return this.http.get<Pokemon[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Pokemon[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Pokemon = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Pokemon[]>): HttpResponse<Pokemon[]> {
        const jsonResponse: Pokemon[] = res.body;
        const body: Pokemon[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Pokemon.
     */
    private convertItemFromServer(pokemon: Pokemon): Pokemon {
        const copy: Pokemon = Object.assign({}, pokemon);
        return copy;
    }

    /**
     * Convert a Pokemon to a JSON which can be sent to the server.
     */
    private convert(pokemon: Pokemon): Pokemon {
        const copy: Pokemon = Object.assign({}, pokemon);
        return copy;
    }
}
