package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.WorkStatusDataSource;

public class WorkStatusLocalDataSource implements WorkStatusDataSource {

    @Nullable
    private static WorkStatusLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private WorkStatusLocalDataSource() {

    }

    public static WorkStatusLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WorkStatusLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public WorkStatus getWorkStatusByUuid(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(WorkStatus.class).equalTo("uuid", uuid).findFirst());
    }

    @Override
    public List<WorkStatus> getWorkStatuses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(WorkStatus.class).findAllSorted("title"));
    }
}