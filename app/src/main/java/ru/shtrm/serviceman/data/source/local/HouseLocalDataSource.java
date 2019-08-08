package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.Street;
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
        List<House> list = realm.where(House.class).findAllSorted("number");
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public List<House> getHousesByStreet(Street street) {
        Realm realm = Realm.getDefaultInstance();
        List<House> list = realm.where(House.class).equalTo("street.uuid", street.getUuid())
                .findAllSorted("number");
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public House getHouse(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        House list = realm.where(House.class).equalTo("uuid", uuid).findFirst();
        if (list != null) {
            list = realm.copyFromRealm(list);
        }

        realm.close();
        return list;
    }

    @Override
    public void updateHouseStatus(final House house, final HouseStatus houseStatus) {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house.setHouseStatus(houseStatus);
                realm.copyToRealmOrUpdate(house);
            }
        });
        realm.close();
    }

}