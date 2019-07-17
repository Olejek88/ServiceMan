package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Message;

public interface MessageDataSource {

    List<Message> getMessages();

    Message getMessage(String uuid);

    void saveMessage(Message message);

    long getLastId();

}
