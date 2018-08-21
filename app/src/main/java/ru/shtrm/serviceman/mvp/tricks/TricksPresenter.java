package ru.shtrm.serviceman.mvp.tricks;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.source.TricksRepository;

public class TricksPresenter implements TricksContract.Presenter {

    @NonNull
    private final TricksContract.View view;

    @NonNull
    private final TricksRepository TricksRepository;

    public TricksPresenter(@NonNull TricksContract.View view,
                           @NonNull TricksRepository TricksRepository) {
        this.view = view;
        this.TricksRepository = TricksRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadTricks();
    }

    @Override
    public void unsubscribe() {
    }

    /**
     * Get all the Tricks from repository and show on view.
     */
    @Override
    public void loadTricks() {
        List<Trick> tricks = TricksRepository.getTricks();
        if (tricks!=null) {
            view.showTricks(tricks);
        }
        else
            view.showEmptyView(true);
    }
}
