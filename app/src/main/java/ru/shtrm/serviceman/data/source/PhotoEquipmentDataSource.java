package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.PhotoEquipment;

public interface PhotoEquipmentDataSource {

    List<PhotoEquipment> getPhotoByEquipment(Equipment equipment);

    List<PhotoEquipment> getPhotoEquipment();

    PhotoEquipment getLastPhotoByEquipment(Equipment equipment);

    void savePhotoEquipment(PhotoEquipment photoEquipment);

    long getLastId();

}
