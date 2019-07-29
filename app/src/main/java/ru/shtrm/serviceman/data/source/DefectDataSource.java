package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.Measure;

public interface DefectDataSource {

    List<Defect> getDefects();

    Defect getDefect(String uuid);

    void addDefect(@NonNull final Defect defect);

    long getLastId();
}
