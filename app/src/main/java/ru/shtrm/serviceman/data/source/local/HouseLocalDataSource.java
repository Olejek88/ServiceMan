package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.source.HouseDataSource;

public class HouseLocalDataSource implements HouseDataSource {

    @Nullable
    private static HouseLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private HouseLocalDataSource() {

    }

    public static HouseLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HouseLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<House> getHousesByStreet(Street street) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(House.class).equalTo("street", street.getUuid()).
                        findAllSorted("title"));
    }
}