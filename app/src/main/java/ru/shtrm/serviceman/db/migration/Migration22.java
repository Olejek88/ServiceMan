package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import ru.shtrm.serviceman.data.User;

public class Migration22 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 21 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Documentation").addRealmObjectField("organization", schema.get("Organization"));
        schema.get("User").addField("type", int.class);
        schema.get("User").transform(new RealmObjectSchema.Function() {
            @Override
            public void apply(DynamicRealmObject obj) {
                obj.set("type", User.Type.WORKER);
            }
        });
    }
}
