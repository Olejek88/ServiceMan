package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.EquipmentSystem;

public interface IEquipmentSystemService {
    @GET("/equipment-system")
    Call<List<EquipmentSystem>> getData(@Query("changedAfter") String changeAfter);
}
