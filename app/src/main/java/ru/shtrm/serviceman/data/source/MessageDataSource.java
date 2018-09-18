package ru.shtrm.serviceman.data.source;

import ru.shtrm.serviceman.data.Message;

public interface MessageDataSource {

    void saveMessage(Message message);

    long getLastId();

}
