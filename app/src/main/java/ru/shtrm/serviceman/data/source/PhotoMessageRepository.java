package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.PhotoMessage;

public class PhotoMessageRepository implements PhotoMessageDataSource {

    @Nullable
    private static PhotoMessageRepository INSTANCE = null;

    @NonNull
    private final PhotoMessageDataSource localDataSource;

    // Prevent direct instantiation
    private PhotoMessageRepository(@NonNull PhotoMessageDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static PhotoMessageRepository getInstance(@NonNull PhotoMessageDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoMessageRepository(localDataSource);
        }
        return INSTANCE;
    }
    @Override
    public void savePhotoMessage(PhotoMessage photoMessage) {
        localDataSource.savePhotoMessage(photoMessage);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
