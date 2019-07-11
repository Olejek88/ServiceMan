package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EquipmentType extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private EquipmentSystem equipmentSystem;
    private Date createdAt;
    private Date changedAt;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public EquipmentSystem getEquipmentSystem() {
        return equipmentSystem;
    }

    public void setEquipmentSystem(EquipmentSystem equipmentSystem) {
        this.equipmentSystem = equipmentSystem;
    }

    class Type {
        public static final String EQUIPMENT_HVS = "7AB0B720-9FDB-448C-86C1-4649A7FCF279";
        public static final String EQUIPMENT_GVS = "4F50C767-A044-465B-A69F-02DD321BC5FB";
        public static final String EQUIPMENT_ELECTRICITY = "B6904443-363B-4F01-B940-F47B463E66D8";
        public static final String EQUIPMENT_HEAT_COUNTER = "42686CFC-34D0-45FF-95A4-04B0D865EC35";

        public static final String EQUIPMENT_TYPE_BALCONY = "24AAD5EA-E4D9-4C8A-8ABB-F8261C24006E";
        public static final String EQUIPMENT_HVS_COUNTER = "3AB137FC-1944-4DD4-9116-81BC8BFD4818";
        public static final String EQUIPMENT_HVS_MAIN = "89D725FC-1A88-44BC-AB12-FBD4D2F8937C";
        public static final String EQUIPMENT_HVS_PUMP = "7C6BD837-1262-4737-9F88-5F14761ED729";
        public static final String EQUIPMENT_HVS_TOWER = "326C0544-F552-4381-B022-254FBC94EA65";

        public static final String EQUIPMENT_GVS_MAIN = "B38595F4-EAF1-43BD-84F5-EE2C1B7F52FA";
        public static final String EQUIPMENT_GVS_PUMP = "39FEC794-2785-4C1E-84AA-C14AEBCE0C8C";
        public static final String EQUIPMENT_GVS_TOWER = "04F24BFD-8DB5-4461-A364-A591FA990BD1";

        public static final String EQUIPMENT_HEAT_MAIN = "1C648F81-CE80-46D0-A80E-003F8E1D0EFC";
        public static final String EQUIPMENT_HEAT_TOWER = "5299B161-EDFE-4514-8983-585A87226C1C";
        public static final String EQUIPMENT_HEAT_RADIATOR = "20E4E8C8-9779-4760-8918-42C1C71C071A";
        public static final String EQUIPMENT_HEAT_PUMP = "B52C7DE2-88BA-46E4-848E-8F71CAE3DAF1";

        public static final String EQUIPMENT_ROOF = "6646DEA9-4D1B-4367-8927-F2A0F39C3957";
        public static final String EQUIPMENT_ROOF_ENTRANCE = "2C458E63-26D9-486B-B013-A9D4D6BB46A0";
        public static final String EQUIPMENT_ROOF_ROOM = "FC1B4F06-4140-4987-B068-E4889EED9E86";
        public static final String EQUIPMENT_ROOF_WATER_PIPE = "E9E12F54-78DF-4912-A5BE-AE4F1A9DA10D";

        public static final String EQUIPMENT_WALL = "CC7E730F-2D79-4723-8DB8-7210345C416B";
        public static final String EQUIPMENT_WALL_WATER = "328D5FC1-1EDC-4F7D-BBD8-7CEC2052FEC6";

        public static final String EQUIPMENT_YARD = "776429BD-5D91-40A3-94CD-3280A707EA45";
        public static final String EQUIPMENT_YARD_TBO = "04A9079A-1108-4DAD-B94E-F261CE1C2EF2";
        public static final String EQUIPMENT_YARD_DRENAGE = "A66B3143-9A74-4B24-B299-A4365746B36A";

        public static final String EQUIPMENT_ENTRANCE_WINDOWS = "4986E2B7-B16F-4838-B628-D27948E9D671";
        public static final String EQUIPMENT_ENTRANCE_DOOR = "3927D323-707C-4AED-B76F-109ABDADA1C6";
        public static final String EQUIPMENT_ENTRANCE_TRASH_PIPE = "26FAFF05-6A7A-4EA4-BBA9-CC4A6B5C6B3C";
        public static final String EQUIPMENT_ENTRANCE_STAIRS = "65E63695-E2EF-4D00-9721-B4E41A88B090";

        public static final String EQUIPMENT_LIFT = "C7987543-EA90-4EB0-894F-547E6F0A753A";
        public static final String EQUIPMENT_ENTRANCE_DOOR_TAMBUR = "5DEC5E96-5C08-4CB1-8033-5157D0DAC062";
        public static final String EQUIPMENT_ENTRANCE_MAIN = "9CADDCE4-DEA1-4CAC-8824-B3610B6F0E66";

        public static final String EQUIPMENT_SEWER_PIPE = "E3231B90-93D9-4054-8A2D-367A2CC751AA";
        public static final String EQUIPMENT_SEWER_MAIN = "64CF1076-32B0-4E5F-B401-06330C07C111";
        public static final String EQUIPMENT_SEWER_WELL = "BABE06AD-FE44-4475-BF7B-22A141C70160";

        public static final String EQUIPMENT_ELECTRICITY_COUNTER = "2511ACA5-EFBA-4956-846D-5B24F1DDF394";
        public static final String EQUIPMENT_ELECTRICITY_VRU = "F907BF72-101D-4396-AC8E-701F1821F6C5";
        public static final String EQUIPMENT_ELECTRICITY_LEVEL_SHIELD = "4BA33570-CEBD-41E8-8778-B29AAC29B78B";
        public static final String EQUIPMENT_ELECTRICITY_LIGHT = "843D578C-62D3-4DAC-81D0-19CA9EBCAB3C";
        public static final String EQUIPMENT_ELECTRICITY_ENTRANCE_LIGHT = "87DB7F49-A98F-4CD3-B670-F005D89920AE";
        public static final String EQUIPMENT_ELECTRICITY_ENTRANCE_PIPE = "D235FA7F-774F-4FB7-A12E-EA6D08D61D1D";
        public static final String EQUIPMENT_ELECTRICITY_HERD = "053D2E52-BD3D-4549-9207-E96D4C053E89";

        public static final String EQUIPMENT_INTERNET = "7E6BB6A1-C91C-494C-9011-9D0A4472798E";
        public static final String EQUIPMENT_CONDITIONER = "D8C3809A-3A5C-4835-BBBE-8D8C196737D7";
        public static final String EQUIPMENT_DOMOPHONE = "002F8B45-6783-470E-A759-D0272803794C";
        public static final String EQUIPMENT_TV = "0D7EAF5E-315F-4348-A339-C00254E773A7";

        public static final String EQUIPMENT_GAS = "07C0BBB4-3594-405B-8829-A8F3330AC27F";
        public static final String EQUIPMENT_BASEMENT = "FDBE2840-12D3-4182-AFC1-072A1CEFB44A";
        public static final String EQUIPMENT_BASEMENT_ROOM = "C7FDE255-3C2B-49B7-8D7D-C7F93057D4D9";
        public static final String EQUIPMENT_BASEMENT_WINDOWS = "41CD23B4-D250-4FBD-9ED7-7FF788A3BA20";

    }
}
