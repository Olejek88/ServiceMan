package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.DocumentationType;
import ru.shtrm.serviceman.data.source.DocumentationTypeDataSource;

public class DocumentationTypeLocalDataSource implements DocumentationTypeDataSource {

    @Nullable
    private static DocumentationTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private DocumentationTypeLocalDataSource() {

    }

    public static DocumentationTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DocumentationTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<DocumentationType> getAllDocumentationTypes() {
        Realm realm = Realm.getDefaultInstance();
        List<DocumentationType> list = realm.where(DocumentationType.class)
                .findAllSorted("title", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }
}