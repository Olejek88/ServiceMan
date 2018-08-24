package ru.shtrm.serviceman.db;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.User;

public class LoadTestData {
    public static User user;
    public static Street street;
    public static House house1;
    public static House house2;
    public static Flat flat;
    private static Equipment equipment1;

    public static HouseStatus houseStatus;
    public static FlatStatus flatStatus;

    public static EquipmentStatus equipmentStatus;
    public static EquipmentType equipmentType;


    public static void LoadAllTestData() {

        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();

        final String userTestUuid = "4462ed77-9bf0-4542-b127-f4ecefce49da";
        final String streetTestUuid = "5562ed77-9bf0-4542-b127-f4ecefce49da";
        final String streetUuid = "1dd8d4f8-5c98-4444-86ed-97dddde";
        final String houseUuid1 = "1dd8d4f8-5c98-4444-86ed-97ddddf";
        final String houseUuid2 = "00000000-5c98-4444-86ed-97dddde";
        final String flatUuid = "1dd4d4f8-5c98-4444-86ed-97ddddf";

        final String equipmentUuid1 = "1dd8d4f8-5c98-4444-86ed-97ddbc2059f6";
        final String equipmentUuid2 = "1dd8d4f8-5c98-4444-86ed-97aabc2059f6";
        final String equipmentUuid3 = "1dd8d4f8-5c98-5445-86ed-97ddbc2059f6";

        final String houseStatusUuid = "1dd8d4f8-5c98-4444-86ed-97dddd2059f6";
        final String flatStatusUuid = "1dd8d4f8-5c98-4444-86ed-97dddd2019f6";

        final String equipmentStatusUuid = "1dd8d4f8-5c98-4444-86ed-973332123213";
        final String equipmentTypeUuid = "1dd8d4f8-5c98-4444-86ed-971212121212";

        // User --------------------
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user = realmDB.createObject(User.class, "1");
                user.setUuid(userTestUuid);
                user.setName("Иванов О.А.");
                user.setPin("1234");
                user.setContact("+79227000285 Иван");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                houseStatus = realmDB.createObject(HouseStatus.class,"1");
                houseStatus.setUuid(houseStatusUuid);
                houseStatus.setTitle("Все в порядке");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flatStatus = realmDB.createObject(FlatStatus.class,"1");
                flatStatus.setUuid(flatStatusUuid);
                flatStatus.setTitle("Все в порядке");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipmentStatus = realmDB.createObject(EquipmentStatus.class,"1");
                equipmentStatus.setUuid(equipmentStatusUuid);
                equipmentStatus.setTitle("Работоспособно");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipmentType = realmDB.createObject(EquipmentType.class,"1");
                equipmentType.setUuid(equipmentTypeUuid);
                equipmentType.setTitle("Датчик расхода ХВ");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                street = realmDB.createObject(Street.class,"1");
                street.setUuid(streetUuid);
                street.setTitle("Воронежская");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house1 = realmDB.createObject(House.class,"1");
                house1.setUuid(houseUuid1);
                house1.setTitle("8А");
                house1.setHouseStatus(houseStatus);
            }
        });
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house2 = realmDB.createObject(House.class,"2");
                house2.setUuid(houseUuid2);
                house2.setTitle("6");
                house2.setHouseStatus(houseStatus);
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flat = realmDB.createObject(Flat.class,"1");
                flat.setUuid(flatUuid);
                flat.setTitle("8");
                flat.setFlatStatus(flatStatus);
            }
        });


        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipment1 = realmDB.createObject(Equipment.class,"1");
                equipment1.setUuid(equipmentUuid1);
                equipment1.setFlat(flat);
                equipment1.setHouse(house1);
                equipment1.setSerial("12345");
            }
        });

        realmDB.close();
    }

    public static void DeleteSomeData() {
        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //realmDB.where(Equipment.class).findAll().deleteAllFromRealm();
            }
        });

        realmDB.close();
    }
}
