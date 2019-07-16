package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.UserHouse;

public class UserHouseRepository implements UserHouseDataSource {

    @Nullable
    private static UserHouseRepository INSTANCE = null;

    @NonNull
    private final UserHouseDataSource localDataSource;

    // Prevent direct instantiation
    private UserHouseRepository(@NonNull UserHouseDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static UserHouseRepository getInstance(@NonNull UserHouseDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UserHouseRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<UserHouse> getAllUserHouses() {
        return localDataSource.getAllUserHouses();
    }

    @Override
    public List<UserHouse> getHousesByUser(@NonNull String userUuid) {
        return localDataSource.getHousesByUser(userUuid);
    }
}
