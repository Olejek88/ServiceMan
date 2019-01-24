package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.OperationDataSource;
import ru.shtrm.serviceman.data.source.TaskDataSource;

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
        return realm.copyFromRealm(
                realm.where(Operation.class).equalTo("task.uuid", task.getUuid()).
                        findAllSorted("createdAt", Sort.DESCENDING));
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