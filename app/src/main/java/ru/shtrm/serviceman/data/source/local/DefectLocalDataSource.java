package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.source.DefectDataSource;

public class DefectLocalDataSource implements DefectDataSource {

    @Nullable
    private static DefectLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private DefectLocalDataSource() {

    }

    public static DefectLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefectLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Defect> getDefects() {
        Realm realm = Realm.getDefaultInstance();
        List<Defect> list = realm.where(Defect.class)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findAllSorted("title");
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public Defect getDefect(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Defect list = realm.where(Defect.class).equalTo("uuid", uuid)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findFirst();
        if (list != null) {
            list = realm.copyFromRealm(list);
        }

        realm.close();
        return list;
    }

    @Override
    public void addDefect(@NonNull Defect defect) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(defect);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public long getLastId() {
        return Defect.getLastId();
    }

}