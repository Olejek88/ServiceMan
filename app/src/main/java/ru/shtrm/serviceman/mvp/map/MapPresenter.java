package ru.shtrm.serviceman.mvp.map;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.StreetRepository;
import ru.shtrm.serviceman.mvp.abonents.AbonentsContract;

public class MapPresenter implements MapContract.Presenter {

    @NonNull
    private final MapContract.View view;

    @NonNull
    private final HouseRepository houseRepository;

    public MapPresenter(@NonNull MapContract.View view,
                        @NonNull HouseRepository houseRepository) {
        this.view = view;
        this.houseRepository = houseRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadHouses();
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public void loadHouses() {
        List<House> houses = houseRepository.getHouses();
        view.showHouses(houses);
    }
}
