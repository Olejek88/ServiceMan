package ru.shtrm.serviceman.mvp.flat;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.FlatStatusRepository;

public class FlatPresenter implements FlatContract.Presenter {

    @NonNull
    private FlatContract.View view;

    @NonNull
    private FlatRepository flatsRepository;

    @NonNull
    private FlatStatusRepository flatStatusRepository;

    @NonNull
    private EquipmentRepository equipmentRepository;

    public FlatPresenter(@NonNull FlatContract.View view,
                               @NonNull FlatRepository flatsRepository,
                                @NonNull EquipmentRepository equipmentRepository,
                                @NonNull FlatStatusRepository flatStatusRepository) {
        this.view = view;
        this.flatsRepository = flatsRepository;
        this.flatStatusRepository = flatStatusRepository;
        this.equipmentRepository = equipmentRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public List<Equipment> loadEquipmentsByFlat(Flat flat){
        List<Equipment> equipment = equipmentRepository.getEquipmentByFlat(flat);
        view.showEquipments(equipment);
        return equipment;
    }

    @Override
    public void addFlat(Flat flat) {
        flatsRepository.addFlat(flat);
    }

    @Override
    public void updateFlatStatus(Flat flat, FlatStatus flatStatus) {
        flatsRepository.updateFlatStatus(flat, flatStatus);
    }

    @Override
    public List<FlatStatus> loadFlatStatuses() {
        return flatStatusRepository.getFlatStatuses();
    }
}
