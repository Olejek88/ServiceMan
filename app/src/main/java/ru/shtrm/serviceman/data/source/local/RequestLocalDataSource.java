package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Request;
import ru.shtrm.serviceman.data.source.RequestDataSource;

public class RequestLocalDataSource implements RequestDataSource {

    @Nullable
    private static RequestLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private RequestLocalDataSource() {

    }

    public static RequestLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RequestLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Request getRequest(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Request request = realm.where(Request.class).equalTo("uuid", uuid).findFirst();
        if (request != null) {
            request = realm.copyFromRealm(request);
            realm.close();
            return request;
        } else {
            return null;
        }
    }

    @Override
    public List<Request> getRequests() {
        Realm realm = Realm.getDefaultInstance();
        List<Request> requests = realm.copyFromRealm(
                realm.where(Request.class).
                        findAllSorted("createdAt", Sort.ASCENDING));
        realm.close();
        return requests;
    }


    @Override
    public Request getRequestByTask(String taskUuid) {
        Realm realm = Realm.getDefaultInstance();
        if (taskUuid != null) {
            Request request = realm.where(Request.class).
                    equalTo("task.uuid", taskUuid).findFirst();
            if (request != null) {
                request = realm.copyFromRealm(request);
                realm.close();
                return request;
            }
        }
        return null;
    }
}