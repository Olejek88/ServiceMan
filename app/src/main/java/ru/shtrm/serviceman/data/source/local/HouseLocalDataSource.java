package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.UserHouse;
import ru.shtrm.serviceman.data.source.HouseDataSource;

public class HouseLocalDataSource implements HouseDataSource {

    @Nullable
    private static HouseLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private HouseLocalDataSource() {

    }

    public static HouseLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HouseLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<House> getHouses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(House.class).findAllSorted("title"));
    }

    @Override
    public List<House> getHousesByStreet(Street street) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(House.class).equalTo("street.uuid", street.getUuid()).
                        findAllSorted("title"));
    }

    @Override
    public List<House> getHousesByStreetForCurrentUser(Street street) {
        Realm realm = Realm.getDefaultInstance();
        User user = AuthorizedUser.getInstance().getUser();
        List<House> resultHouse = new ArrayList<>();
        List<House> housesByStreet = realm.copyFromRealm(realm.where(House.class).
                equalTo("street.uuid", street.getUuid()).findAllSorted("number"));
        List<UserHouse> housesByUser = realm.copyFromRealm(realm.where(UserHouse.class).
                equalTo("user.uuid", user.getUuid()).findAll());
        for (House house : housesByStreet) {
            for (UserHouse userHouse : housesByUser) {
                if (house.getUuid().equals(userHouse.getUuid()))
                    resultHouse.add(house);
            }
        }
        return resultHouse;
    }

    @Override
    public House getHouse(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(House.class).equalTo("uuid", uuid).
                        findFirst());
    }
}