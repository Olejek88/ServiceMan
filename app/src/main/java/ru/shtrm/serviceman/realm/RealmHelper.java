package ru.shtrm.serviceman.realm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmHelper {

    public static final String DATABASE_NAME = "askmaster.realm";

    public static Realm newRealmInstance() {
        return Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
    }

}
