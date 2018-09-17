package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.EquipmentType;

public interface IEquipmentTypeService {
    @GET("/equipment-type")
    Call<List<EquipmentType>> getData(@Query("changedAfter") String changeAfter);
}
