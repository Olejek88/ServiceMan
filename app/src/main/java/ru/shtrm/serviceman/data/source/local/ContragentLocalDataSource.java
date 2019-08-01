package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Contragent;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.ContragentDataSource;
import ru.shtrm.serviceman.data.source.TaskDataSource;

public class ContragentLocalDataSource implements ContragentDataSource {

    @Nullable
    private static ContragentLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ContragentLocalDataSource() {

    }

    public static ContragentLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContragentLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Contragent getContragent(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Contragent contragent = realm.where(Contragent.class).equalTo("uuid", uuid).findFirst();
        if (contragent != null) {
            contragent = realm.copyFromRealm(contragent);
            realm.close();
            return contragent;
        } else {
            return null;
        }
    }

    @Override
    public List<Contragent> getContragents() {
        Realm realm = Realm.getDefaultInstance();
        List<Contragent> contragents = realm.copyFromRealm(
                realm.where(Contragent.class).findAllSorted("createdAt", Sort.ASCENDING));
        realm.close();
        return contragents;
    }
}