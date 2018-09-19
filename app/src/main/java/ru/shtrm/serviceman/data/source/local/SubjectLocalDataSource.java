package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Subject;
import ru.shtrm.serviceman.data.source.SubjectDataSource;

public class SubjectLocalDataSource implements SubjectDataSource {

    @Nullable
    private static SubjectLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private SubjectLocalDataSource() {

    }

    public static SubjectLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SubjectLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Subject getSubject(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Subject subject = realm.where(Subject.class).equalTo("uuid", uuid).findFirst();
        if (subject != null)
            return realm.copyFromRealm(subject);
        else
            return null;
    }

    @Override
    public Subject getSubjectByFlat(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Subject subject = realm.where(Subject.class).equalTo("flat.uuid", uuid).findFirst();
        if (subject != null)
            return realm.copyFromRealm(subject);
        else
            return null;
    }

    @Override
    public Subject getSubjectByHouse(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Subject subject = realm.where(Subject.class).equalTo("house.uuid", uuid).findFirst();
        if (subject != null)
            return realm.copyFromRealm(subject);
        else
            return null;
    }
}