package ru.shtrm.serviceman.mvp.map;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.HouseRepository;

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
