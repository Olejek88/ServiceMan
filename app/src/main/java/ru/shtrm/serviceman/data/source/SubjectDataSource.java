package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.Subject;

public interface SubjectDataSource {

    Subject getSubject(@NonNull String uuid);

    Subject getSubjectByHouse(@NonNull String uuid);

    Subject getSubjectByFlat(@NonNull String uuid);
}
