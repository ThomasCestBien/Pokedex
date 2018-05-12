/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokebaseTestModule } from '../../../test.module';
import { PokedexComponent } from '../../../../../../main/webapp/app/entities/pokedex/pokedex.component';
import { PokedexService } from '../../../../../../main/webapp/app/entities/pokedex/pokedex.service';
import { Pokedex } from '../../../../../../main/webapp/app/entities/pokedex/pokedex.model';

describe('Component Tests', () => {

    describe('Pokedex Management Component', () => {
        let comp: PokedexComponent;
        let fixture: ComponentFixture<PokedexComponent>;
        let service: PokedexService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [PokedexComponent],
                providers: [
                    PokedexService
                ]
            })
            .overrideTemplate(PokedexComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PokedexComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PokedexService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Pokedex(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pokedexes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
