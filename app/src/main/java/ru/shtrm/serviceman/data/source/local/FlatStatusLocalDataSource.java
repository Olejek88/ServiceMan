package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.FlatDataSource;
import ru.shtrm.serviceman.data.source.FlatStatusDataSource;

public class FlatStatusLocalDataSource implements FlatStatusDataSource {

    @Nullable
    private static FlatStatusLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private FlatStatusLocalDataSource() {

    }

    public static FlatStatusLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FlatStatusLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<FlatStatus> getFlatStatuses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(FlatStatus.class).findAllSorted("title"));
    }
}