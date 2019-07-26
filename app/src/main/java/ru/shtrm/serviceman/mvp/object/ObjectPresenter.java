package ru.shtrm.serviceman.mvp.object;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.ObjectRepository;

public class ObjectPresenter implements ObjectContract.Presenter {

    @NonNull
    private ObjectContract.View view;

    @NonNull
    private ObjectRepository objectRepository;

    @NonNull
    private EquipmentRepository equipmentRepository;

    ObjectPresenter(@NonNull ObjectContract.View view,
                    @NonNull ObjectRepository objectRepository,
                    @NonNull EquipmentRepository equipmentRepository) {
        this.view = view;
        this.objectRepository = objectRepository;
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
    public List<Equipment> loadEquipmentsByObject(ZhObject object){
        List<Equipment> equipment = equipmentRepository.getEquipmentByObject(object);
        view.showEquipments(equipment);
        return equipment;
    }
}
