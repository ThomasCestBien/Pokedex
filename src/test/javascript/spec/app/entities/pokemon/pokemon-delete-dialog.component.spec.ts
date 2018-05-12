/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { PokebaseTestModule } from '../../../test.module';
import { PokemonDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/pokemon/pokemon-delete-dialog.component';
import { PokemonService } from '../../../../../../main/webapp/app/entities/pokemon/pokemon.service';

describe('Component Tests', () => {

    describe('Pokemon Management Delete Component', () => {
        let comp: PokemonDeleteDialogComponent;
        let fixture: ComponentFixture<PokemonDeleteDialogComponent>;
        let service: PokemonService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [PokebaseTestModule],
                declarations: [PokemonDeleteDialogComponent],
                providers: [
                    PokemonService
                ]
            })
            .overrideTemplate(PokemonDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PokemonDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PokemonService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
