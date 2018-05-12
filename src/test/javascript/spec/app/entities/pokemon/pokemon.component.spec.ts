/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokebaseTestModule } from '../../../test.module';
import { PokemonComponent } from '../../../../../../main/webapp/app/entities/pokemon/pokemon.component';
import { PokemonService } from '../../../../../../main/webapp/app/entities/pokemon/pokemon.service';
import { Pokemon } from '../../../../../../main/webapp/app/entities/pokemon/pokemon.model';

describe('Component Tests', () => {

    describe('Pokemon Management Component', () => {
        let comp: PokemonComponent;
        let fixture: ComponentFixture<PokemonComponent>;
        let service: PokemonService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [PokemonComponent],
                providers: [
                    PokemonService
                ]
            })
            .overrideTemplate(PokemonComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PokemonComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PokemonService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Pokemon(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pokemons[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
