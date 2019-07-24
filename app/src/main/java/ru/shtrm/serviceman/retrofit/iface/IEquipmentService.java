package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.UpdateQuery;

public interface IEquipmentService {
    @GET("/equipment")
    Call<List<Equipment>> getData(@Query("changedAfter") String changeAfter);
    @POST("/equipment/create")
    Call<ResponseBody> sendData(@Body List<Equipment> data);

    @POST("/equipment/update-attribute")
    Call<ResponseBody> updateAttribute(@Body UpdateQuery attribute);
}
