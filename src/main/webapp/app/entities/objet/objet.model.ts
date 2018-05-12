import { BaseEntity } from './../../shared';

export const enum TypeObj {
    'POTION',
    'BALL',
    'PIERRE',
    'BAIE',
    'CT_CS'
}

export class Objet implements BaseEntity {
    constructor(
        public id?: number,
        public objname?: string,
        public type?: TypeObj,
        public img?: string,
        public pokedex?: BaseEntity,
    ) {
    }
}
