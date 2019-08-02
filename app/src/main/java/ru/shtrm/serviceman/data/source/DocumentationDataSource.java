package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Documentation;

public interface DocumentationDataSource {

    List<Documentation> getDocumentations();

    Documentation getDocumentation(String uuid);

    Documentation getDocumentationByEquipment(String uuid);
}
