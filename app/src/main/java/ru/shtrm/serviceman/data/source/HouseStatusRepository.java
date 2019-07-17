package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.HouseStatus;

public class HouseStatusRepository implements HouseStatusDataSource {

    @Nullable
    private static HouseStatusRepository INSTANCE = null;

    @NonNull
    private final HouseStatusDataSource localDataSource;

    // Prevent direct instantiation
    private HouseStatusRepository(@NonNull HouseStatusDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static HouseStatusRepository getInstance(@NonNull HouseStatusDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new HouseStatusRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<HouseStatus> getHouseStatuses() {
        return localDataSource.getHouseStatuses();
    }

    @Override
    public HouseStatus getHouseStatus(String uuid) {
        return localDataSource.getHouseStatus(uuid);
    }

}
