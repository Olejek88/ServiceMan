package ru.shtrm.serviceman.mvp.equipment;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.EquipmentStatusRepository;
import ru.shtrm.serviceman.data.source.GpsTrackRepository;

public class EquipmentPresenter implements EquipmentContract.Presenter {

    @NonNull
    private EquipmentContract.View view;

    @NonNull
    private EquipmentStatusRepository equipmentStatusRepository;


    public EquipmentPresenter(@NonNull EquipmentContract.View view,
                                @NonNull EquipmentRepository equipmentRepository,
                                @NonNull EquipmentStatusRepository equipmentStatusRepository,
                              @NonNull GpsTrackRepository gpsTrackRepository,
                              @NonNull String flatId) {
        this.view = view;
        this.equipmentStatusRepository = equipmentStatusRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public void updateEquipmentStatus(Equipment equipment, EquipmentStatus equipmentStatus) {
        //equipmentRepository.updateEquipmentStatus(equipment, equipmentStatus);
    }

    @Override
    public List<EquipmentStatus> loadEquipmentStatuses() {
        return equipmentStatusRepository.getEquipmentStatuses();
    }
}
