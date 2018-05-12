import { BaseEntity } from './../../shared';

export const enum TypePo {
    'ACIER',
    'COMBAT',
    'DRAGON',
    'EAU',
    'ELECTRIK',
    'FEE',
    'FEU',
    'GLACE',
    'PSY',
    'INSECTE',
    'NORMAL',
    'SOL',
    'ROCHE',
    'PLANTE',
    'VOL',
    'POISON',
    'SPECTRE',
    'TENEBRE'
}

export class Pokemon implements BaseEntity {
    constructor(
        public id?: number,
        public pokename?: string,
        public numero?: number,
        public type?: TypePo,
        public type2?: TypePo,
        public lowevo?: number,
        public uppevo?: number,
        public img?: string,
        public pokedex?: BaseEntity,
    ) {
    }
}
