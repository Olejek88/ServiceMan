package ru.shtrm.serviceman.db.migration;

import io.realm.DynamicRealm;

public interface IAppMigration {
    void migration(DynamicRealm realm);
}
