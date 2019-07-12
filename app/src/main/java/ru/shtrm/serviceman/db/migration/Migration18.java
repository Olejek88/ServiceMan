package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import ru.shtrm.serviceman.data.User;

public class Migration18 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 17 version");
        RealmSchema schema = realm.getSchema();

        schema.get("User").transform(new RealmObjectSchema.Function() {
            @Override
            public void apply(DynamicRealmObject obj) {
                if (obj.get("uuid").equals(User.SERVICE_USER_UUID)) {
                    obj.set("pin", User.SERVICE_USER_PIN);
                }
            }
        });

        schema.get("Equipment").removeField("flat");
        schema.get("Message").removeField("flat");
        schema.get("Task").removeField("flat");
        schema.get("PhotoAlarm").removeField("flat");

        schema.remove("Resident");
        schema.remove("Subject");
        schema.remove("PhotoFlat");

        schema.remove("Flat");
        schema.remove("FlatStatus");
        schema.remove("FlatType");
    }
}
