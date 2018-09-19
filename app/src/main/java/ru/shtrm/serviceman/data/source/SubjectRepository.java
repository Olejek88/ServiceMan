package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.shtrm.serviceman.data.Subject;

public class SubjectRepository implements SubjectDataSource {

    @Nullable
    private static SubjectRepository INSTANCE = null;

    @NonNull
    private final SubjectDataSource localDataSource;

    // Prevent direct instantiation
    private SubjectRepository(@NonNull SubjectDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static SubjectRepository getInstance(@NonNull SubjectDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new SubjectRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Subject getSubject(@NonNull String uuid) {
        return localDataSource.getSubject(uuid);
    }

    @Override
    public Subject getSubjectByFlat(@NonNull String uuid) {
        return localDataSource.getSubjectByFlat(uuid);
    }

    @Override
    public Subject getSubjectByHouse(@NonNull String uuid) {
        return localDataSource.getSubjectByHouse(uuid);
    }

}
