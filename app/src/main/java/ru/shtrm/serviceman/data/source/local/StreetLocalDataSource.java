package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.source.HouseDataSource;
import ru.shtrm.serviceman.data.source.StreetDataSource;

public class StreetLocalDataSource implements StreetDataSource {

    @Nullable
    private static StreetLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private StreetLocalDataSource() {

    }

    public static StreetLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StreetLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Street> getStreets() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Street.class).findAllSorted("title"));
    }
}