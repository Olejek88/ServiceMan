package ru.shtrm.serviceman.db;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import dalvik.system.DexFile;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import ru.shtrm.serviceman.db.migration.Migration1;
import ru.shtrm.serviceman.db.migration.Migration11;
import ru.shtrm.serviceman.db.migration.Migration2;
import ru.shtrm.serviceman.db.migration.Migration3;
import ru.shtrm.serviceman.db.migration.Migration4;
import ru.shtrm.serviceman.db.migration.Migration5;
import ru.shtrm.serviceman.db.migration.Migration6;
import ru.shtrm.serviceman.db.migration.Migration7;
import ru.shtrm.serviceman.db.migration.Migration8;
import ru.shtrm.serviceman.db.migration.Migration9;
import ru.shtrm.serviceman.db.migration.Migration10;

class AppRealmMigration implements RealmMigration {
    private final String TAG = this.getClass().getName();
    private Context context;

    AppRealmMigration(Context context) {
        this.context = context;
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        Log.d(TAG, "oldVersion = " + oldVersion);
        Log.d(TAG, "newVersion = " + newVersion);

        if (oldVersion == newVersion) {
//            if (!testPropsFields(realm)) {
//                throw new RealmException("Классы и схема не идентичны!!!");
//            }

            return;
        }

        if (oldVersion == 0) {
            new Migration1().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 1) {
            new Migration2().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 2) {
            new Migration3().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 3) {
            new Migration4().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 4) {
            new Migration5().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 5) {
            new Migration6().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 6) {
            new Migration7().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 7) {
            new Migration8().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 8) {
            new Migration9().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 9) {
            new Migration10().migration(realm);
            oldVersion++;
        }

        if (oldVersion == 10) {
            new Migration11().migration(realm);
            oldVersion++;
        }
        //testPropsFields(realm);
    }

    private boolean testPropsFields(DynamicRealm realm) {
        RealmSchema schema = realm.getSchema();

        // проверяем соответствие схемы базы со свойствами классов
        Set<RealmObjectSchema> realmObjects = schema.getAll();
        Set<String> tableList = new LinkedHashSet<>();
        for (RealmObjectSchema realmObject : realmObjects) {
            String tableName = realmObject.getClassName();
            Log.d(TAG, "Class name = " + tableName);
            tableList.add(tableName);
            Field[] classProps;
            Set<String> props = new HashSet<>();
            Map<String, String> propsType = new HashMap<>();
            try {
                Class<?> c = Class.forName("ru.shtrm.serviceman.data." + tableName);
                classProps = c.getDeclaredFields();
                for (Field prop : classProps) {
                    props.add(prop.getName());
                    propsType.put(prop.getName(), prop.getType().getName());
//                    propsType.put(prop.getName(), prop.getGenericType().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // проверяем количество и названия полей и свойств
            Set<String> fieldNames = realmObject.getFieldNames();
            Set<String> backProps = new HashSet<>(props);
            props.removeAll(fieldNames);
            fieldNames.removeAll(backProps);
            if (props.size() == 0 && fieldNames.size() == 0) {
                Log.d(TAG, "Список полей идентичен.");
            } else {
                StringBuilder b = new StringBuilder();
                if (props.size() > 0) {
                    for (String item : props) {
                        b.append(item).append(", ");
                    }

                    Log.e(TAG, "Список свойств класса без соответствующих полей в таблице: " + b.toString());
                }

                if (fieldNames.size() > 0) {
                    b.setLength(0);
                    for (String item : fieldNames) {
                        b.append(item).append(", ");
                    }

                    Log.e(TAG, "Список полей таблицы без соответствующих свойств класса: " + b.toString());
                }

                return false;
            }

            // сравниваем типы свойств и полей
            for (String fieldName : fieldNames) {
                String realmType = realmObject.getFieldType(fieldName).name();
                String propType = propsType.get(fieldName);
                if (!realmType.equals(getType(propType))) {
                    Log.e(TAG, "Type not same (fName = " + fieldName + "): fType = " + realmType + ", pType = " + propType);
                    return false;
                }
            }
        }

        // TODO: реализовать загрузку classes.dex и поиск в нём <Lru.toir.mobile.db.realm.*;>
        // получаем список классов объектов которые выступают в роли таблиц
        Set<String> classList = new HashSet<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            Enumeration<String> iter = df.entries();
            while (iter.hasMoreElements()) {
                String classPath = iter.nextElement();
                if (classPath.contains("ru.shtrm.serviceman.data") && !classPath.contains("$")) {
                    try {
                        Class<?> driverClass = Class.forName(classPath);
                        if (!driverClass.isInterface()) {

                            Constructor<?> constructor = driverClass.getConstructor();
                            RealmObject o = (RealmObject) constructor.newInstance();
                            o.getClass().getMethod("deleteFromRealm");
                            classList.add(classPath.substring(classPath.lastIndexOf('.') + 1));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // проверяем соответствие полученых списков классов и таблиц
        Set<String> backTableList = new HashSet<>(tableList);
        tableList.removeAll(classList);
        classList.removeAll(backTableList);
        if (tableList.size() == 0 && classList.size() == 0) {
            Log.d(TAG, "Список классов соответствует списку таблиц.");
        } else {
            StringBuilder b = new StringBuilder();
            if (tableList.size() > 0) {
                for (String item : tableList) {
                    b.append(item).append(", ");
                }

                Log.e(TAG, "Список таблиц без соответствующих классов: " + b.toString());
            }

            if (classList.size() > 0) {
                b.setLength(0);
                for (String item : classList) {
                    b.append(item).append(", ");
                }

                Log.e(TAG, "Список классов без соответствующих таблиц: " + b.toString());
            }

            return false;
        }

        return true;
    }

    private String getType(String type) {
        String result = type.substring(type.lastIndexOf('.') + 1).toUpperCase();

        switch (result) {
            case "INT":
            case "LONG":
                result = "INTEGER";
                break;
            case "STRING":
            case "DOUBLE":
            case "DATE":
            case "FLOAT":
            case "BOOLEAN":
                break;
            case "REALMLIST":
                result = "LIST";
                break;
            default:
                result = "OBJECT";
        }

        return result;
    }

}
