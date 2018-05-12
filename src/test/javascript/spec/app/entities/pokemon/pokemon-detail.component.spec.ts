/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PokebaseTestModule } from '../../../test.module';
import { PokemonDetailComponent } from '../../../../../../main/webapp/app/entities/pokemon/pokemon-detail.component';
import { PokemonService } from '../../../../../../main/webapp/app/entities/pokemon/pokemon.service';
import { Pokemon } from '../../../../../../main/webapp/app/entities/pokemon/pokemon.model';

describe('Component Tests', () => {

    describe('Pokemon Management Detail Component', () => {
        let comp: PokemonDetailComponent;
        let fixture: ComponentFixture<PokemonDetailComponent>;
        let service: PokemonService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [PokemonDetailComponent],
                providers: [
                    PokemonService
                ]
            })
            .overrideTemplate(PokemonDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PokemonDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PokemonService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Pokemon(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.pokemon).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
