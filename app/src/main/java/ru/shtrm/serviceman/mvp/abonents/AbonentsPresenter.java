package ru.shtrm.serviceman.mvp.abonents;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.StreetRepository;

public class AbonentsPresenter implements AbonentsContract.Presenter {

    @NonNull
    private final AbonentsContract.View view;

    @NonNull
    private final StreetRepository streetRepository;

    @NonNull
    private final HouseRepository houseRepository;

    @NonNull
    private final FlatRepository flatRepository;

    public AbonentsPresenter(@NonNull AbonentsContract.View view,
                             @NonNull StreetRepository streetRepository,
                             @NonNull HouseRepository houseRepository,
                             @NonNull FlatRepository flatRepository) {
        this.view = view;
        this.streetRepository = streetRepository;
        this.houseRepository = houseRepository;
        this.flatRepository = flatRepository;
        this.view.setPresenter(this);
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
        view.showStreets(streets);
        return streets;
    }

    @Override
    public List<House> loadHouses(Street street) {
        List<House> houses = houseRepository.getHousesByStreet(street);
        view.showHouses(houses);
        return houses;
    }

    @Override
    public List<Flat> loadFlats(House house) {
        List<Flat> flats = flatRepository.getFlatsByHouse(house);
        view.showFlats(flats);
        return flats;
    }

    @Override
    public void updateAbonent(Flat flat, Equipment equipment) {

    }

}
