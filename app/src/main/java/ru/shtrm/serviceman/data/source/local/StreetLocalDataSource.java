package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.UserHouse;
import ru.shtrm.serviceman.data.source.StreetDataSource;

public class StreetLocalDataSource implements StreetDataSource {

    @Nullable
    private static StreetLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private StreetLocalDataSource() {

    }

    public static StreetLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StreetLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Street> getStreets() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Street.class).findAllSorted("title"));
    }

    @Override
    public List<Street> getStreetsByCurrentUser() {
        boolean present;
        Realm realm = Realm.getDefaultInstance();
        User user = UsersLocalDataSource.getInstance().getAuthorisedUser();
        List<Street> resultStreet = new ArrayList<>();
        List<Street> allStreet = realm.copyFromRealm(
                realm.where(Street.class).findAllSorted("title"));
        if (user==null)
            return resultStreet;
        for (Street street : allStreet) {
            present = false;
            List<House> housesByStreet = realm.copyFromRealm(realm.where(House.class).
                    equalTo("street.uuid", street.getUuid()).findAllSorted("title"));
            List<UserHouse> housesByUser = realm.copyFromRealm(realm.where(UserHouse.class).
                    equalTo("user.uuid", user.getUuid()).findAll());
            for (House house : housesByStreet) {
                for (UserHouse userHouse : housesByUser) {
                    if (house.getUuid().equals(userHouse.getUuid())) {
                        resultStreet.add(street);
                        present = true;
                        break;
                    }
                }
                if (present) break;
            }
        }
        return resultStreet;
    }
}