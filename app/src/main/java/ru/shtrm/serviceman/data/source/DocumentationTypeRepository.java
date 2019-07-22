package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.DefectType;
import ru.shtrm.serviceman.data.DocumentationType;

public class DocumentationTypeRepository implements DocumentationTypeDataSource {

    @Nullable
    private static DocumentationTypeRepository INSTANCE = null;

    @NonNull
    private final DocumentationTypeDataSource localDataSource;

    // Prevent direct instantiation
    private DocumentationTypeRepository(@NonNull DocumentationTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static DocumentationTypeRepository getInstance(@NonNull DocumentationTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DocumentationTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<DocumentationType> getAllDocumentationTypes() {
        return localDataSource.getAllDocumentationTypes();
    }
}
