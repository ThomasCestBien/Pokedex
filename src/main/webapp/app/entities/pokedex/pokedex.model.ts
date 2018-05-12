import { BaseEntity } from './../../shared';

export class Pokedex implements BaseEntity {
    constructor(
        public id?: number,
        public pokedexname?: string,
        public pokemons?: BaseEntity[],
        public objets?: BaseEntity[],
    ) {
    }
}
