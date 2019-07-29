package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Documentation;


public class DocumentationRepository implements DocumentationDataSource {

    @Nullable
    private static DocumentationRepository INSTANCE = null;

    @NonNull
    private final DocumentationDataSource localDataSource;

    // Prevent direct instantiation
    private DocumentationRepository(@NonNull DocumentationDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static DocumentationRepository getInstance(@NonNull DocumentationDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DocumentationRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Documentation> getDocumentations() {
        return localDataSource.getDocumentations();
    }

    @Override
    public Documentation getDocumentation(String uuid) {
        return localDataSource.getDocumentation(uuid);
    }

}
