package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.PhotoMessage;

public class MessageRepository implements MessageDataSource {

    @Nullable
    private static MessageRepository INSTANCE = null;

    @NonNull
    private final MessageDataSource localDataSource;

    // Prevent direct instantiation
    private MessageRepository(@NonNull MessageDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static MessageRepository getInstance(@NonNull MessageDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepository(localDataSource);
        }
        return INSTANCE;
    }
    @Override
    public void saveMessage(Message message) {
        localDataSource.saveMessage(message);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
