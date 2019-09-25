package ru.shtrm.serviceman.mvp.abonents;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.ObjectRepository;
import ru.shtrm.serviceman.data.source.StreetRepository;

public class AbonentsPresenter implements AbonentsContract.Presenter {

    @NonNull
    private final AbonentsContract.View view;

    @NonNull
    private final StreetRepository streetRepository;

    @NonNull
    private final HouseRepository houseRepository;

    @NonNull
    private final ObjectRepository flatRepository;

    public AbonentsPresenter(@NonNull AbonentsContract.View view,
                             @NonNull StreetRepository streetRepository,
                             @NonNull HouseRepository houseRepository,
                             @NonNull ObjectRepository flatRepository) {
        this.view = view;
        this.streetRepository = streetRepository;
        this.houseRepository = houseRepository;
        this.flatRepository = flatRepository;
        this.view.setPresenter(this);

        // просто тупо реагируем на все изменения в базе realm один раз
        // при удалении MapPresenter необходимо удалять RealmChangeListener!
        Realm realm = Realm.getDefaultInstance();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                switch (AbonentsPresenter.this.view.getCurrentLevel()) {
                    case AbonentsFragment.LEVEL_STREET:
                        AbonentsPresenter.this.loadStreets();
                        break;
                    case AbonentsFragment.LEVEL_HOUSE:
                        AbonentsPresenter.this.loadHouses(AbonentsPresenter.this.view.getCurrentStreet());
                        break;
                    case AbonentsFragment.LEVEL_FLAT:
                        AbonentsPresenter.this.loadObjects(AbonentsPresenter.this.view.getCurrentHouse());
                        break;
                }
            }
        });
        realm.close();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public List<Street> loadStreets() {
        List<Street> streets = streetRepository.getStreets();
        //TODO вариант для выбора только по пользователю
        //List<Street> streets = streetRepository.getStreetsByCurrentUser();
        view.showStreets(streets);
        return streets;
    }

    @Override
    public List<House> loadHouses(Street street) {
        List<House> houses = houseRepository.getHousesByStreet(street);
        //TODO вариант для выбора только по пользователю
        //List<House> houses = houseRepository.getHousesByStreetForCurrentUser(street);
        view.showHouses(houses);
        return houses;
    }

    @Override
    public List<ZhObject> loadObjects(House house) {
        List<ZhObject> flats = flatRepository.getObjectsByHouse(house);
        view.showObjects(flats);
        return flats;
    }
}
