package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Contragent;
import ru.shtrm.serviceman.data.TaskType;


public class ContragentRepository implements ContragentDataSource {

    @Nullable
    private static ContragentRepository INSTANCE = null;

    @NonNull
    private final ContragentDataSource localDataSource;

    // Prevent direct instantiation
    private ContragentRepository(@NonNull ContragentDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ContragentRepository getInstance(@NonNull ContragentDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ContragentRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Contragent> getContragents() {
        return localDataSource.getContragents();
    }

    @Override
    public Contragent getContragent(String uuid) {
        return localDataSource.getContragent(uuid);
    }

    @Override
    public Contragent getContragentByObject(String uuid) {
        return localDataSource.getContragentByObject(uuid);
    }

}
