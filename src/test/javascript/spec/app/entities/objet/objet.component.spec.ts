/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PokebaseTestModule } from '../../../test.module';
import { ObjetComponent } from '../../../../../../main/webapp/app/entities/objet/objet.component';
import { ObjetService } from '../../../../../../main/webapp/app/entities/objet/objet.service';
import { Objet } from '../../../../../../main/webapp/app/entities/objet/objet.model';

describe('Component Tests', () => {

    describe('Objet Management Component', () => {
        let comp: ObjetComponent;
        let fixture: ComponentFixture<ObjetComponent>;
        let service: ObjetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [ObjetComponent],
                providers: [
                    ObjetService
                ]
            })
            .overrideTemplate(ObjetComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ObjetComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ObjetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Objet(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.objets[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
