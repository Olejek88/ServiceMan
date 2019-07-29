package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.UserHouse;

public interface UserHouseDataSource {

    List<UserHouse> getAllUserHouses();

    List<UserHouse> getHousesByUser(@NonNull String userUuid);
}
