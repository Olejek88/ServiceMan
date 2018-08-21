package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Trick;

public interface TricksDataSource {

    List<Trick> getTricks();

    Trick getTrick(@NonNull final String id);

    void saveTrick(@NonNull Trick trick, @NonNull final ArrayList<Image> images);

    void deleteTrick(@NonNull String id);

}
