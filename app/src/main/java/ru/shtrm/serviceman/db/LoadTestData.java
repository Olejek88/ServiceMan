package ru.shtrm.serviceman.db;

import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.FlatType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.Subject;
import ru.shtrm.serviceman.data.User;

public class LoadTestData {
    public static User user;
    public static City city;
    public static Street street;
    private static House house1;
    private static House house2;
    private static House house3;

    public static Flat flat;
    public static Flat flat2;

    private static Equipment equipment1;

    private static HouseStatus houseStatus;

    private static FlatStatus flatStatus;
    private static FlatStatus flatStatus2;

    private static FlatType flatType;

    private static HouseType houseType;
    private static HouseType houseType2;

    public static EquipmentStatus equipmentStatus;
    public static EquipmentType equipmentType;

    private static Alarm alarm1;
    private static Alarm alarm2;
    public static AlarmStatus alarmStatus;
    public static AlarmType alarmType;
    public static AlarmType alarmType2;

    public static Resident resident;
    public static Subject subject;

    final static String flatUuid = "1dd4d4f8-5c98-4444-86ed-97ddddf";
    final static String flatUuid2 = "1dd4d4f8-5c98-4444-86ed-97deedf";
    final static String flatUuid3 = "98238371-9c98-5344-86ed-97deedf";
    final static String flatUuid4 = "98238371-9c98-8123-86ed-97deedf";

    final static String equipmentUuid1 = "1dd8d4f8-5c98-4444-86ed-97ddbc2059f6";
    final static String equipmentUuid2 = "1dd8d4f8-5c98-4444-86ed-97aabc2059f6";
    final static String equipmentUuid3 = "1dd8d4f8-5c98-5445-86ed-97ddbc2059f6";

    final static String houseStatusUuid = "1dd8d4f8-5c98-4444-86ed-97dddd2059f6";
    final static String flatStatusUuid = "1dd8d4f8-5c98-4444-86ed-97dddd2019f6";
    final static String flatStatusUuid2 = "6de8d7f8-5c98-4444-86ed-97dddd2019f6";

    final static String equipmentStatusUuid = "1dd8d4f8-5c98-4444-86ed-973332123213";
    final static String equipmentTypeUuid = "1dd8d4f8-5c98-4444-86ed-971212121212";

    final static String residentUuid = "1dd8d4f8-5c98-5885-86ed-97ddbc2059f6";

    final static String equipmentUuid4 = "23023912-5c98-5445-86ed-97ddbc2059f6";
    final static String flatTypeUuid = "1dd8d4f8-5c98-4534-86ed-97dddd2019f6";
    final static String houseTypeUuid = "1dd8d4f8-5c98-4534-86ed-21821929192A";
    final static String houseTypeUuid2 = "91273A38-5c98-4534-86ed-21821929192A";

    final static String userTestUuid = "4462ed77-9bf0-4542-b127-f4ecefce49da";
    final static String cityUuid = "5562ed77-9bf0-4542-b127-ffffefce49da";
    final static String streetUuid = "1dd8d4f8-5c98-4444-86ed-97dddde";
    final static String houseUuid1 = "1dd8d4f8-5c98-4444-86ed-97ddddf";
    final static String houseUuid2 = "00000000-5c98-4444-86ed-97dddde";
    final static String houseUuid3 = "12345678-5c98-4444-86ed-97dddde";
    final static String subjectUuid = "82128989-5c98-4444-86ed-97dddde";

    public static void LoadTestUser() {
        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();

        final String userTestUuid = "4462ed77-9bf0-4542-b127-f4ecefce49da";
        // User --------------------
        user = realmDB.where(User.class).findFirst();
        if (user == null) {
            realmDB.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    user = realmDB.createObject(User.class, 1);
                    user.setUuid(userTestUuid);
                    user.setName("Иванов О.А.");
                    user.setPin("1234");
                    user.setContact("+79227000293");
                }
            });
        }
    }

    public static void LoadAllTestData() {

        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();

        final String alarmStatusUuid = "4462ed77-9bf0-4542-b127-f4ecefce4321";
        final String alarmTypeUuid = "5562ed77-9bf0-4542-b127-ffffefce1234";
        final String alarmUuid1 = "5562ed77-9bf0-4542-b127-ffffefce0011";
        final String alarmUuid2 = "5562ed77-9bf0-4542-b127-ffffefce0022";

        final String cityUuid = "5562ed77-9bf0-4542-b127-ffffefce49da";
        final String streetUuid = "1dd8d4f8-5c98-4444-86ed-97dddde";
        final String houseUuid1 = "1dd8d4f8-5c98-4444-86ed-97ddddf";
        final String houseUuid2 = "00000000-5c98-4444-86ed-97dddde";

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                city = realmDB.createObject(City.class, 1);
                city.setTitle("Нязепетровск");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                houseStatus = realmDB.createObject(HouseStatus.class, 1);
                houseStatus.setUuid(houseStatusUuid);
                houseStatus.setTitle("Все в порядке");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flatStatus = realmDB.createObject(FlatStatus.class, 1);
                flatStatus.setUuid(flatStatusUuid);
                flatStatus.setTitle("Все в порядке");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipmentStatus = realmDB.createObject(EquipmentStatus.class, 1);
                equipmentStatus.setUuid(equipmentStatusUuid);
                equipmentStatus.setTitle("Работоспособно");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipmentType = realmDB.createObject(EquipmentType.class, 1);
                equipmentType.setUuid(equipmentTypeUuid);
                equipmentType.setTitle("Датчик расхода ХВ");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                street = realmDB.createObject(Street.class, 1);
                street.setUuid(streetUuid);
                street.setCity(city);
                street.setTitle("Воронежская");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house1 = realmDB.createObject(House.class, 1);
                house1.setUuid(houseUuid1);
                house1.setStreet(street);
                house1.setTitle("8А");
                house1.setHouseStatus(houseStatus);
            }
        });
        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house2 = realmDB.createObject(House.class, 2);
                house2.setUuid(houseUuid2);
                house2.setStreet(street);
                house2.setTitle("6");
                house2.setHouseStatus(houseStatus);
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flat = realmDB.createObject(Flat.class, 1);
                flat.setUuid(flatUuid);
                flat.setHouse(house1);
                flat.setTitle("8");
                flat.setFlatStatus(flatStatus);
            }
        });


        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipment1 = realmDB.createObject(Equipment.class, 1);
                equipment1.setUuid(equipmentUuid1);
                equipment1.setFlat(flat);
                equipment1.setHouse(house1);
                equipment1.setSerial("12345");
            }
        });

        realmDB.close();
    }

    public static void LoadAllTestData2() {

        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();

        final String alarmStatusUuid = "4462ed77-9bf0-4542-b127-f4ecefce4321";
        final String alarmTypeUuid = "5562ed77-9bf0-4542-b127-ffffefce1234";
        final String alarmTypeUuid2 = "5562ed77-9bf0-4542-b127-ffffefce1235";
        final String alarmUuid1 = "5562ed77-9bf0-4542-b127-ffffefce0011";
        final String alarmUuid2 = "5562ed77-9bf0-4542-b127-ffffefce0022";

        user = realmDB.where(User.class).findFirst();

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarmStatus = realmDB.createObject(AlarmStatus.class, 1);
                alarmStatus.setUuid(alarmTypeUuid);
                alarmStatus.setTitle("Зафиксирован/Не устранен");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarmType = realmDB.createObject(AlarmType.class, 1);
                alarmType.setUuid(alarmTypeUuid);
                alarmType.setTitle("Незаконная врезка");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarmType2 = realmDB.createObject(AlarmType.class, 2);
                alarmType2.setUuid(alarmTypeUuid);
                alarmType2.setTitle("Протечка");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarm1 = realmDB.createObject(Alarm.class, 1);
                alarm1.setUuid(alarmUuid1);
                alarm1.setAlarmStatus(alarmStatus);
                alarm1.setAlarmType(alarmType);
                alarm1.setUser(user);
                alarm1.setComment("Идет открыто вдоль трубы налево");
                alarm1.setDate(new Date());
                alarm1.setLatitude(56.03);
                alarm1.setLongitude(59.36);
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarm2 = realmDB.createObject(Alarm.class, 2);
                alarm2.setUuid(alarmUuid2);
                alarm2.setAlarmStatus(alarmStatus);
                alarm2.setAlarmType(alarmType);
                alarm2.setUser(user);
                alarm2.setComment("Течет вода из трубы");
                alarm2.setDate(new Date());
                alarm2.setLatitude(56.02);
                alarm2.setLongitude(59.36);
            }
        });

        realmDB.close();
    }

    public static void LoadAllTestData3() {
        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();

        user = realmDB.where(User.class).findFirst();
        house1 = realmDB.where(House.class).findFirst();
        flatStatus = realmDB.where(FlatStatus.class).findFirst();
        equipmentType = realmDB.where(EquipmentType.class).findFirst();
        equipmentStatus = realmDB.where(EquipmentStatus.class).findFirst();

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flat = realmDB.createObject(Flat.class, 3);
                flat.setUuid(flatUuid2);
                flat.setHouse(house1);
                flat.setTitle("12");
                flat.setFlatStatus(flatStatus);
                flat.setCreatedAt(new Date());
                flat.setChangedAt(new Date());
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipment1 = realmDB.createObject(Equipment.class, 2);
                equipment1.setUuid(equipmentUuid2);
                equipment1.setFlat(flat);
                equipment1.setHouse(house1);
                equipment1.setSerial("123459876");
                equipment1.setEquipmentType(equipmentType);
                equipment1.setEquipmentStatus(equipmentStatus);
                equipment1.setTestDate(new Date());
                equipment1.setChangedAt(new Date());
                equipment1.setCreatedAt(new Date());
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipment1 = realmDB.createObject(Equipment.class, 3);
                equipment1.setUuid(equipmentUuid3);
                equipment1.setFlat(flat);
                equipment1.setHouse(house1);
                equipment1.setSerial("523459876");
                equipment1.setEquipmentType(equipmentType);
                equipment1.setEquipmentStatus(equipmentStatus);
                equipment1.setTestDate(new Date());
                equipment1.setChangedAt(new Date());
                equipment1.setCreatedAt(new Date());
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                resident = realmDB.createObject(Resident.class, 1);
                resident.setUuid(residentUuid);
                resident.setFlat(flat);
                resident.setInn("652839378161");
                resident.setOwner("Иванов О.А.");
                resident.setChangedAt(new Date());
                resident.setCreatedAt(new Date());
            }
        });

        realmDB.close();
    }

    public static void LoadAllTestData4() {
        final Realm realmDB;
        realmDB = Realm.getDefaultInstance();

        user = realmDB.where(User.class).findFirst();
        house2 = realmDB.where(House.class).findFirst();
        flatStatus = realmDB.where(FlatStatus.class).findFirst();
        street = realmDB.where(Street.class).findFirst();
        houseStatus = realmDB.where(HouseStatus.class).findFirst();

        equipmentType = realmDB.where(EquipmentType.class).findFirst();
        equipmentStatus = realmDB.where(EquipmentStatus.class).findFirst();

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flatType = realmDB.createObject(FlatType.class, 1);
                flatType.setUuid(flatTypeUuid);
                flatType.setTitle("Вводный узел");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                houseType = realmDB.createObject(HouseType.class, 1);
                houseType.setUuid(houseTypeUuid);
                houseType.setTitle("Частный дом");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flatStatus = realmDB.createObject(FlatStatus.class, 2);
                flatStatus.setUuid(flatStatusUuid2);
                flatStatus.setTitle("Недоступна");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                houseType2 = realmDB.createObject(HouseType.class, 2);
                houseType2.setUuid(houseTypeUuid2);
                houseType2.setTitle("Многоквартирный дом");
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house2 = realmDB.createObject(House.class, 3);
                house2.setUuid(houseUuid3);
                house2.setStreet(street);
                house2.setTitle("16А");
                house2.setHouseType(houseType);
                house2.setHouseStatus(houseStatus);
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                house3 = realmDB.createObject(House.class, 4);
                house3.setUuid(houseUuid3);
                house3.setStreet(street);
                house3.setTitle("19");
                house3.setHouseType(houseType2);
                house3.setHouseStatus(houseStatus);
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flat = realmDB.createObject(Flat.class, 5);
                flat.setUuid(flatUuid3);
                flat.setHouse(house2);
                flat.setTitle("Котельная");
                flat.setFlatType(flatType);
                flat.setFlatStatus(flatStatus);
                flat.setCreatedAt(new Date());
                flat.setChangedAt(new Date());
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flat2 = realmDB.createObject(Flat.class, 6);
                flat2.setUuid(flatUuid4);
                flat2.setHouse(house3);
                flat2.setTitle("Теплопункт");
                flat2.setFlatType(flatType);
                flat2.setFlatStatus(flatStatus2);
                flat2.setCreatedAt(new Date());
                flat2.setChangedAt(new Date());
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipment1 = realmDB.createObject(Equipment.class, 18);
                equipment1.setUuid(equipmentUuid4);
                equipment1.setFlat(flat);
                equipment1.setHouse(house2);
                equipment1.setSerial("54223459876");
                equipment1.setEquipmentType(equipmentType);
                equipment1.setEquipmentStatus(equipmentStatus);
                equipment1.setTestDate(new Date());
                equipment1.setChangedAt(new Date());
                equipment1.setCreatedAt(new Date());
            }
        });

        realmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                subject = realmDB.createObject(Subject.class, 1);
                subject.setUuid(subjectUuid);
                subject.setHouse(house2);
                subject.setOwner("Колдовский П.С.");
                subject.setContractNumber("АВ2017-128199112");
                subject.setContractDate(new Date());
                subject.setChangedAt(new Date());
                subject.setCreatedAt(new Date());
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
                realmDB.where(Alarm.class).findAll().deleteAllFromRealm();
                realmDB.where(AlarmStatus.class).findAll().deleteAllFromRealm();
                realmDB.where(AlarmType.class).findAll().deleteAllFromRealm();
            }
        });

        realmDB.close();
    }
}
