package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Defect;

public interface DefectDataSource {

    List<Defect> getDefects();

    Defect getDefect(String uuid);
}
