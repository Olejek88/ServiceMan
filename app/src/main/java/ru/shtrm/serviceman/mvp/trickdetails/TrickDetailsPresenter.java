package ru.shtrm.serviceman.mvp.trickdetails;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.source.TricksRepository;

public class TrickDetailsPresenter implements TrickDetailsContract.Presenter {

    @NonNull
    private TrickDetailsContract.View view;

    @NonNull
    private TricksRepository tricksRepository;

    @NonNull
    private String trickId;

    TrickDetailsPresenter(@NonNull String id,
                                 @NonNull TricksRepository tricksRepository,
                                 @NonNull TrickDetailsContract.View trickDetailView) {
        this.trickId = id;
        this.view = trickDetailView;
        this.tricksRepository = tricksRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        openDetail();
    }

    @Override
    public void unsubscribe() {
    }

    /**
     * Load data from repository.
     */
    private void openDetail() {
        view.showTrickDetails(tricksRepository.getTrick(trickId));
    }

    /**
     * Delete the Trick from repository(both in cache and database).
     */
    @Override
    public void deleteTrick() {
        tricksRepository.deleteTrick(trickId);
        view.exit();
    }
}
