package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoMessage;

public interface PhotoMessageDataSource {

    void savePhotoMessage(PhotoMessage photoMessage);

    long getLastId();

}
