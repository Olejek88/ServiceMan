package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Equipment;

public interface IEquipmentService {
    @GET("/equipment")
    Call<List<Equipment>> getData(@Query("changedAfter") String changeAfter);
    @POST("/equipment/create")
    Call<Void> sendData(@Body List<Equipment> data);
}
