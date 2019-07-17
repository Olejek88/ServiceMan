package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.source.DocumentationDataSource;


public class DocumentationLocalDataSource implements DocumentationDataSource {

    @Nullable
    private static DocumentationLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private DocumentationLocalDataSource() {

    }

    public static DocumentationLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DocumentationLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Documentation> getDocumentations() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(realm.where(Documentation.class)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findAllSorted("title"));
    }

    @Override
    public Documentation getDocumentation(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Documentation.class).equalTo("uuid", uuid).findFirst());
    }
}