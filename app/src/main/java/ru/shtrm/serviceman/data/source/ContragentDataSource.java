package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Contragent;
import ru.shtrm.serviceman.data.TaskType;

public interface ContragentDataSource {

    List<Contragent> getContragents();

    Contragent getContragent(String uuid);

}
