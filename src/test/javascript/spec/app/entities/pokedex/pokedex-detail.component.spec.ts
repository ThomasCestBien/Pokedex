/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PokebaseTestModule } from '../../../test.module';
import { PokedexDetailComponent } from '../../../../../../main/webapp/app/entities/pokedex/pokedex-detail.component';
import { PokedexService } from '../../../../../../main/webapp/app/entities/pokedex/pokedex.service';
import { Pokedex } from '../../../../../../main/webapp/app/entities/pokedex/pokedex.model';

describe('Component Tests', () => {

    describe('Pokedex Management Detail Component', () => {
        let comp: PokedexDetailComponent;
        let fixture: ComponentFixture<PokedexDetailComponent>;
        let service: PokedexService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [PokedexDetailComponent],
                providers: [
                    PokedexService
                ]
            })
            .overrideTemplate(PokedexDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PokedexDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PokedexService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Pokedex(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.pokedex).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
