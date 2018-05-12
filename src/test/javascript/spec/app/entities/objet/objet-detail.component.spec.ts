/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { PokebaseTestModule } from '../../../test.module';
import { ObjetDetailComponent } from '../../../../../../main/webapp/app/entities/objet/objet-detail.component';
import { ObjetService } from '../../../../../../main/webapp/app/entities/objet/objet.service';
import { Objet } from '../../../../../../main/webapp/app/entities/objet/objet.model';

describe('Component Tests', () => {

    describe('Objet Management Detail Component', () => {
        let comp: ObjetDetailComponent;
        let fixture: ComponentFixture<ObjetDetailComponent>;
        let service: ObjetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [ObjetDetailComponent],
                providers: [
                    ObjetService
                ]
            })
            .overrideTemplate(ObjetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ObjetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ObjetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Objet(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.objet).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
