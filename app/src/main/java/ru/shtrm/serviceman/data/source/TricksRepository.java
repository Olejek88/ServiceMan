package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Trick;

public class TricksRepository implements TricksDataSource {

    @Nullable
    private static TricksRepository INSTANCE = null;

    @NonNull
    private final TricksDataSource tricksRemoteDataSource;

    @NonNull
    private final TricksDataSource tricksLocalDataSource;

    private Map<String, Trick> cachedTricks;

    // Prevent direct instantiation
    private TricksRepository(@NonNull TricksDataSource tricksRemoteDataSource,
                             @NonNull TricksDataSource tricksLocalDataSource) {
        this.tricksRemoteDataSource = tricksRemoteDataSource;
        this.tricksLocalDataSource = tricksLocalDataSource;
    }

    // The access for other classes.
    public static TricksRepository getInstance(@NonNull TricksDataSource tricksRemoteDataSource,
                                               @NonNull TricksDataSource tricksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TricksRepository(tricksRemoteDataSource, tricksLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * It is designed to gotten the Tricks from both database
     * and network. Which are faster then return them.
     * @return Tricks from {@link ru.shtrm.serviceman.data.source.local.TricksLocalDataSource}.
     */
    @Override
    public List<Trick> getTricks() {
        return tricksLocalDataSource.getTricks();
    }

    /**
     * Get a Trick of specific number from data source.
     * @param id The primary key or the Trick id. See {@link Trick}.
     * @return The Trick.
     */
    @Override
    public Trick getTrick(@NonNull final String id) {
        return tricksLocalDataSource.getTrick(id);
    }

    /**
     * Save the Trick to data source and cache.
     * It is supposed to save it to database and network too.
     * But we have no cloud(The account system) yet.
     * It may change either.
     * @param trick The Trick to save. See more @{@link Trick}.
     */
    @Override
    public void saveTrick(@NonNull Trick trick, @NonNull final ArrayList<Image> images) {
        tricksLocalDataSource.saveTrick(trick, images);
    }

    /**
     * Delete a Trick from data source and cache.
     * @param trickId The primary id or in another words, the Trick number.
     *                  See more @{@link Trick#id}.
     */
    @Override
    public void deleteTrick(@NonNull String trickId) {
        tricksLocalDataSource.deleteTrick(trickId);
    }
}
