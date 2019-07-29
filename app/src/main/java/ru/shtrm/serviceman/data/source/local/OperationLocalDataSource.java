package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.OperationDataSource;

public class OperationLocalDataSource implements OperationDataSource {

    @Nullable
    private static OperationLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private OperationLocalDataSource() {

    }

    public static OperationLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OperationLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Operation> getOperationByTask(Task task) {
        Realm realm = Realm.getDefaultInstance();
        List<Operation> list = realm.where(Operation.class).equalTo("task.uuid", task.getUuid())
                .findAllSorted("createdAt", Sort.DESCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public void setOperationStatus(final Operation operation, final WorkStatus status) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                operation.setWorkStatus(status);
                realm.copyToRealmOrUpdate(operation);
            }
        });
        realm.close();
    }
}